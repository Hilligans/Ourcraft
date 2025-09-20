package dev.hilligans.engine.mod.handler.content;

import dev.hilligans.engine.GameInstance;
import dev.hilligans.ourcraft.Ourcraft;
import dev.hilligans.engine.mod.handler.ModClass;
import dev.hilligans.engine.resource.dataloader.FolderResourceDirectory;
import dev.hilligans.engine.util.argument.Argument;
import dev.hilligans.planets.Planets;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.ServiceLoader;
import java.util.function.Consumer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ModList {

    public static Argument<Boolean> devBuild = Argument.existArg("--devBuild")
            .help("Used to disable manual class loading, required when developing in intellij.");

    public ArrayList<ModContainer> mods = new ArrayList<>();
    public GameInstance gameInstance;

    public HashSet<Class<?>> classList = new HashSet<>();

    public ModList(GameInstance gameInstance) {
        this.gameInstance = gameInstance;
    }

    public ModList load() {
        if(new File("ourcraft-1.0.3-jar-with-dependencies.jar").exists()) {
            gameInstance.DATA_LOADER.addJar("ourcraft-1.0.3-jar-with-dependencies.jar", "ourcraft");
        } else if(new File("ourcraft-1.0.3.jar").exists()) {
            gameInstance.DATA_LOADER.addJar("ourcraft-1.0.3.jar", "ourcraft");
        } else {
        //    gameInstance.DATA_LOADER.addFolder("target/classes/", "ourcraft");
        }

        if(!(devBuild.get(gameInstance))) {
            for(ModClass modClass : ServiceLoader.load(ModClass.class)) {
                this.mods.add(new ModContainer(modClass));
                this.classList.add(modClass.getClass());
            }
        }

        loadAllMods(new File("mods/"));
        loadClasses(new File("target/classes/"), "");

        gameInstance.DATA_LOADER.addStream();

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
                        classList.add(testClass);

                        Class<ModClass> clazz = testClass(testClass);
                        if(clazz != null) {
                            ModContainer container = new ModContainer(clazz, child, new File("target/classes/").toPath());
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
                classList.add(testClass);

                Class<ModClass> clazz = testClass(testClass);
                if(clazz != null) {
                    ModContainer container = new ModContainer(clazz, child, file.toPath());
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

    public ModContainer getMod(String modID) {
        return mods.stream().filter((mod) -> mod.getModID().equals(modID)).findFirst().orElse(null);
    }
}
