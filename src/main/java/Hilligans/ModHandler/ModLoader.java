package Hilligans.ModHandler;

import Hilligans.Block.Block;
import Hilligans.Client.Client;
import Hilligans.Data.Primitives.DoubleTypeWrapper;
import Hilligans.Data.Primitives.TripleTypeWrapper;

import java.io.File;
import java.io.FileInputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
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
                        String modID = getModID(testClass);
                        if(modID != null) {
                            this.mod = modID;
                            mainClasses.put(modID,new TripleTypeWrapper<>(testClass,mod.getAbsolutePath(),false));
                            testClass.newInstance();
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
                    mod = modID;
                    testClass = Class.forName(name,true,child);
                    mainClasses.put(modID,new TripleTypeWrapper<>(testClass,file.getAbsolutePath(),true));
                    try {
                        testClass.newInstance();
                    } catch (NoClassDefFoundError | Exception e) {
                        new Exception("Failed to load mod " + modID + " from " + file.getName(),e).printStackTrace();
                    }
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
