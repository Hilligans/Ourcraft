package dev.Hilligans.ourcraft.Resource;

import java.util.ArrayList;
import java.util.HashMap;

public class UniversalResourceLoader {

    public ArrayList<ResourceLoader<?>> resourceLoaders = new ArrayList<>();
    public HashMap<String, ResourceLoader<?>> extensionLoaders = new HashMap<>();

    public HashMap<String, ArrayList<ResourceLoader<?>>> categories = new HashMap<>();
    public HashMap<String, HashMap<String, ResourceLoader<?>>> extensionCategories = new HashMap<>();

    public ClassLoader classLoader = UniversalResourceLoader.class.getClassLoader();

    public synchronized UniversalResourceLoader add(ResourceLoader<?> resourceLoader) {
        resourceLoaders.add(resourceLoader);
        HashMap<String, ResourceLoader<?>> extensions = getExtensionCategory(resourceLoader.category);
        for(String string : resourceLoader.fileTypes) {
            extensionLoaders.put(string,resourceLoader);
            extensions.put(string, resourceLoader);
        }
        getCategory(resourceLoader.category).add(resourceLoader);

        return this;
    }

    public Object getResource(String path) {
        return getResource(path, "");
    }

    public Object getResource(String path, String category) {
        HashMap<String, ResourceLoader<?>> loaders = category.equals("") ? extensionLoaders : extensionCategories.get(category);
        if(loaders == null) {
            throw new RuntimeException("Unknown resource category: " + category);
        }
        classLoader.getResource(path);

        return null;
    }

    public ArrayList<ResourceLoader<?>> getCategory(String category) {
        return categories.computeIfAbsent(category, a -> new ArrayList<>());
    }

    public HashMap<String, ResourceLoader<?>> getExtensionCategory(String category) {
        return extensionCategories.computeIfAbsent(category, a -> new HashMap<>());
    }
}
