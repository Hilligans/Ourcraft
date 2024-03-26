package dev.hilligans.ourcraft.mod.handler;

import dev.hilligans.ourcraft.GameInstance;
import dev.hilligans.ourcraft.data.primitives.Triplet;
import dev.hilligans.ourcraft.mod.handler.content.ModContainer;
import dev.hilligans.ourcraft.mod.handler.content.ModContent;
import dev.hilligans.ourcraft.resource.dataloader.FolderResourceDirectory;
import dev.hilligans.ourcraft.resource.dataloader.ResourceDirectory;
import dev.hilligans.ourcraft.resource.dataloader.ZipResourceDirectory;
import dev.hilligans.ourcraft.resource.loaders.ResourceLoader;
import dev.hilligans.ourcraft.util.Settings;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class ModLoader {

    public String mod = "ourcraft";
    public GameInstance gameInstance;
    public boolean suspended = false;
    public HashMap<String, Triplet<Class<?>,String,Boolean>> mainClasses = new HashMap<>();

    public ModLoader(GameInstance gameInstance) {
        this.gameInstance = gameInstance;
    }

    public ModContent requestMod(String modID) {
        return new ModContent(modID, gameInstance);
    }

    public void loadDefaultMods() {
        //System.out.println("Java " + System.getProperty("java.version"));
        loadAllMods(new File("mods/"));
        if(true) {
            // loadClasses1(new File("target/classes/"), "");
        }

        if(new File("ourcraft-1.0.3-jar-with-dependencies.jar").exists()) {
            gameInstance.DATA_LOADER.addJar("ourcraft-1.0.3-jar-with-dependencies.jar", "ourcraft");
        } else if(new File("ourcraft-1.0.3.jar").exists()) {
            gameInstance.DATA_LOADER.addJar("ourcraft-1.0.3.jar", "ourcraft");
        } else {
            gameInstance.DATA_LOADER.addFolder("target/classes/", "ourcraft");
        }
        //gameInstance.CONTENT_PACK.load();
    }

    public void loadAllMods(File folder) {
        File[] mods = folder.listFiles();
        if(mods != null) {
            for (File mod : mods) {
                loadMod(mod);
            }
        }
    }

    public void loadClasses(File folder, String topName) {
        File[] mods = folder.listFiles();
        if(mods != null) {
            for (File mod : mods) {
                if(mod.isDirectory()) {
                    loadClasses(mod, topName + mod.getName() + ".");
                } else if(mod.getName().endsWith(".class")) {
                    try {
                        URLClassLoader child = new URLClassLoader(new URL[]{mod.toURI().toURL()}, this.getClass().getClassLoader());
                        Class<?> testClass = child.loadClass(topName + mod.getName().substring(0,mod.getName().length() - 6));
                       // Class<?> testClass = Class.forName(topName + mod.getName().substring(0,mod.getName().length() - 6),false,child);
                        String modID = getModID(testClass);
                        if(modID != null) {
                            System.out.println("yes");
                            this.mod = modID;
                            ModContent modContent = requestMod(modID);
                            modContent.isJar = false;
                            modContent.path = folder.getPath();
                            FolderResourceDirectory resourceDirectory = new FolderResourceDirectory(new File("target/classes/"));
                            JSONObject jsonObject = getContent(resourceDirectory);
                            if(jsonObject != null) {
                                modContent.readData(jsonObject);
                            }
                            modContent.mainClass = testClass;
                            modContent.addClassLoader(child);
                            mainClasses.put(modID,new Triplet<>(testClass,mod.getAbsolutePath(),false));
                            gameInstance.DATA_LOADER.add(modID, resourceDirectory);
                            //gameInstance.MOD_LIST.registerMod(modContent);
                        }
                    } catch (Exception ignored) {}
                }
            }
        }
    }



    public boolean loadMod(File file) {
        try {
            URLClassLoader child = new URLClassLoader(new URL[]{file.toURI().toURL()}, this.getClass().getClassLoader());
            ArrayList<String> classNames = getClassNames(file);
            for(String name : classNames) {
                Class<?> testClass = Class.forName(name,false,child);
                String modID = getModID(testClass);
                if(modID != null) {
                    ModContent modContent = requestMod(modID);
                    modContent.path = name;
                    ZipResourceDirectory zipResourceDirectory = new ZipResourceDirectory(new ZipFile(file.getPath()), file.getPath());
                    JSONObject jsonObject = getContent(zipResourceDirectory);
                    if(jsonObject != null) {
                        modContent.readData(jsonObject);
                    }
                    mod = modID;
                    testClass = Class.forName(name,true,child);
                    mainClasses.put(modID,new Triplet<>(testClass,file.getAbsolutePath(),true));
                    modContent.mainClass = testClass;
                    modContent.addClassLoader(child);
                    gameInstance.DATA_LOADER.add(modID, zipResourceDirectory);
                    gameInstance.CONTENT_PACK.registerModContent(modContent);
                    return true;
                }
            }
        } catch (Exception e) {
            mod = "";
            return false;
        }
        mod = "";
        return true;
    }

    public JSONObject getContent(ResourceDirectory resourceDirectory) throws Exception {
        try {
            return new JSONObject(ResourceLoader.toString(resourceDirectory.get("mod.json")));
            //return new JSONObject(WorldLoader.readString(classLoader.getResource("mod.json").openStream()));
        } catch (Exception e) {
            if(!Settings.loadModsWithoutInfo) {
                mod = "";
                throw new Exception("Cannot load file");
            }
        }
        return null;
    }

    //https://stackoverflow.com/questions/15720822/how-to-get-names-of-classes-inside-a-jar-file
    public ArrayList<String> getClassNames(File file) {
        ArrayList<String> classNames = new ArrayList<String>();
        try {
            ZipInputStream zip = new ZipInputStream(new FileInputStream(file.getPath()));
            for (ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry()) {
                if (!entry.isDirectory() && entry.getName().endsWith(".class")) {
                    String className = entry.getName().replace('/', '.');
                    classNames.add(className.substring(0, className.length() - ".class".length()));
                }
            }
        } catch (Exception ignored) {}
        return classNames;
    }

    public <T extends Class<ModClass>> T testClass(Class<?> clazz) {
        if(ModClass.class.isAssignableFrom(clazz)) {
            return (T)clazz;
        }
        return null;
    }

    public String getModID(Class<?> val) {
        for(Annotation annotation : val.getAnnotations()) {
            if(annotation.annotationType().equals(Mod.class)) {
                if(((Mod)annotation).modID().equals("")) {
                    System.out.println("MainClass " + val.toString() + " has no modID specified, will not load");
                    return null;
                } else {
                    return ((Mod)annotation).modID();
                }
            }
        }
        return null;
    }

    public synchronized boolean isSuspended() {
        return suspended;
    }
    public synchronized void suspend() {
        suspended = true;
    }
}
