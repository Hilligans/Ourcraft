package Hilligans.ModHandler;

import Hilligans.ModHandler.Content.ContentPack;
import Hilligans.ModHandler.Content.ModContent;
import Hilligans.Data.Primitives.TripleTypeWrapper;
import Hilligans.Ourcraft;
import Hilligans.Util.Settings;
import Hilligans.WorldSave.WorldLoader;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ModLoader {

    public String mod = "ourcraft";

    public HashMap<String, TripleTypeWrapper<Class<?>,String,Boolean>> mainClasses = new HashMap<>();

    public void loadDefaultMods() {
        loadAllMods(new File("mods/"));
        loadClasses(new File("target/classes/"), "");
        Ourcraft.CONTENT_PACK.load();
        Ourcraft.CONTENT_PACK.generateData();
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
                        Class<?> testClass = Class.forName(topName + mod.getName().substring(0,mod.getName().length() - 6),false,child);
                        JSONObject jsonObject = getContent(child);
                        String modID = getModID(testClass);
                        if(modID != null) {
                            this.mod = modID;
                            ModContent modContent = new ModContent(modID);
                            if(jsonObject != null) {
                                modContent.readData(jsonObject);
                            }
                            modContent.mainClass = testClass;
                            Ourcraft.CONTENT_PACK.mods.put(modID,modContent);
                            mainClasses.put(modID,new TripleTypeWrapper<>(testClass,mod.getAbsolutePath(),false));
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
            JSONObject jsonObject = getContent(child);
            for(String name : classNames) {
                Class<?> testClass = Class.forName(name,false,child);
                String modID = getModID(testClass);
                if(modID != null) {
                    ModContent modContent = new ModContent(modID);
                    if(jsonObject != null) {
                        modContent.readData(jsonObject);
                    }
                    mod = modID;
                    testClass = Class.forName(name,true,child);
                    mainClasses.put(modID,new TripleTypeWrapper<>(testClass,file.getAbsolutePath(),true));
                    modContent.mainClass = testClass;
                    Ourcraft.CONTENT_PACK.mods.put(modID,modContent);
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

    public JSONObject getContent(URLClassLoader classLoader) throws Exception {
        try {
            return new JSONObject(WorldLoader.readString(classLoader.getResource("mod.json").openStream()));
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




}
