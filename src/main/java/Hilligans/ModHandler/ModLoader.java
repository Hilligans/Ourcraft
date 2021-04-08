package Hilligans.ModHandler;

import Hilligans.Block.Block;
import Hilligans.Client.Client;

import java.io.File;
import java.io.FileInputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ModLoader {

    public void loadAllMods(File folder) {
        File[] mods = folder.listFiles();
        if(mods != null) {
            for (File mod : mods) {
                loadMod(mod);
            }
        }
    }

    public boolean loadMod(File file) {
        try {
            URLClassLoader child = new URLClassLoader(new URL[]{file.toURI().toURL()}, this.getClass().getClassLoader());
            ArrayList<String> classNames = getClassNames(file);
            Class<?> mainClass = null;
            for(String name : classNames) {
                Class<?> testClass = Class.forName(name,false,child);
                if(isMain(testClass)) {
                    mainClass = Class.forName(name,true,child);
                    break;
                }
            }
            if(mainClass == null) {
                return false;
            }
            Object instance = mainClass.newInstance();
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

    public boolean isMain(Class<?> val) {
        for(Annotation annotation : val.getAnnotations()) {
            return annotation.annotationType().equals(Mod.class);
        }
        return false;
    }






}
