package dev.hilligans.ourcraft.mod.handler.content;

import dev.hilligans.ourcraft.GameInstance;
import dev.hilligans.ourcraft.Ourcraft;
import dev.hilligans.ourcraft.data.primitives.Triplet;
import dev.hilligans.ourcraft.mod.handler.Mod;
import dev.hilligans.ourcraft.mod.handler.ModClass;
import dev.hilligans.ourcraft.resource.dataloader.FolderResourceDirectory;
import dev.hilligans.ourcraft.resource.dataloader.ResourceDirectory;
import dev.hilligans.ourcraft.resource.dataloader.ZipResourceDirectory;
import dev.hilligans.ourcraft.resource.loaders.ResourceLoader;
import dev.hilligans.ourcraft.util.Settings;
import dev.hilligans.planets.Planets;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class ModList {

    public ArrayList<ModContainer> mods = new ArrayList<>();
    public GameInstance gameInstance;

    public ModList(GameInstance gameInstance) {
        this.gameInstance = gameInstance;
    }

    public ModList load() {
        if(new File("ourcraft-1.0.3-jar-with-dependencies.jar").exists()) {
            gameInstance.DATA_LOADER.addJar("ourcraft-1.0.3-jar-with-dependencies.jar", "ourcraft");
        } else if(new File("ourcraft-1.0.3.jar").exists()) {
            gameInstance.DATA_LOADER.addJar("ourcraft-1.0.3.jar", "ourcraft");
        } else {
            gameInstance.DATA_LOADER.addFolder("target/classes/", "ourcraft");
        }

        loadAllMods(new File("mods/"));
        loadClasses(new File("target/classes/"), "");

        if(!gameInstance.ARGUMENTS.getBoolean("--devBuild", false)) {
            mods.add(new ModContainer(new Ourcraft()));
            mods.add(new ModContainer(new Planets()));
        }

        return this;
    }

    public void registerMod(ModContainer modContainer) {
        mods.add(modContainer);

        gameInstance.RESOURCE_MANAGER.classLoaders.add(modContainer.classLoader);
    }

    public ModList foreach(Consumer<ModContainer> consumer) {
        for(ModContainer mod : mods) {
            consumer.accept(mod);
        }
        return this;
    }

    public int getCount() {
        return mods.size();
    }



    public void loadAllMods(File folder) {
        File[] mods = folder.listFiles();
        if(mods != null) {
            for (File mod : mods) {
                loadMod(mod);
            }
        }
    }

    public <T extends Class<ModClass>> T testClass(Class<?> clazz) {
        if(ModClass.class.isAssignableFrom(clazz)) {
            return (T)clazz;
        }
        return null;
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

                        Class<ModClass> clazz = testClass(testClass);
                        if(clazz != null) {
                            ModContainer container = new ModContainer(clazz, child);
                            String modID = container.getModID();

                            FolderResourceDirectory resourceDirectory = new FolderResourceDirectory(new File("target/classes/"));
                            gameInstance.DATA_LOADER.add(modID, resourceDirectory);
                            gameInstance.MOD_LIST.registerMod(container);
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

                Class<ModClass> clazz = testClass(testClass);
                if(clazz != null) {
                    ModContainer container = new ModContainer(clazz, child);
                    String modID = container.getModID();

                    FolderResourceDirectory resourceDirectory = new FolderResourceDirectory(new File("target/classes/"));
                    gameInstance.DATA_LOADER.add(modID, resourceDirectory);
                    gameInstance.MOD_LIST.registerMod(container);
                }
            }
        } catch (Exception e) {
            return false;
        }
        return true;
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
}
