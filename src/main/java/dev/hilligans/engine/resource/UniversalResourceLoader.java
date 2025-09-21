package dev.hilligans.engine.resource;

import dev.hilligans.engine.resource.loaders.ResourceLoader;
import org.jetbrains.annotations.Nullable;

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
        return getResource(new ResourceLocation(path));
    }

    public <T> T getResource(String path, Class<T> clazz) {
        return (T)getResource(path);
    }

    public String getString(ResourceLocation resourceLocation) {
        ResourceLoader<?> resourceLoader = extensionLoaders.get("txt");
        return (String) resourceLoader.read(resourceLocation);
    }

    public String getString(ResourceLocation resourceLocation, String optPrefix, String optSuffix) {
        ResourceLoader<?> resourceLoader = extensionLoaders.get("txt");

        Object result = resourceLoader.read(resourceLocation.with(optPrefix, optSuffix));
        if(result != null) {
            return (String) result;
        }

        return (String) resourceLoader.read(resourceLocation);
    }

    public Object getResource(ResourceLocation resourceLocation) {
        String extension = getExtension(resourceLocation.path);
        if(extension == null) {
            return null;
        }
        ResourceLoader<?> resourceLoader = extensionLoaders.get(extension);
        if(resourceLoader == null) {
            throw new RuntimeException("No Resource Loader found for extension: " + extension);
        }
        return resourceLoader.read(resourceLocation);
    }

    public <T> T getResource(ResourceLocation resourceLocation, Class<T> clazz) {
        return (T)getResource(resourceLocation);
    }

    public Object getUnknownResource(String path) {
        return getUnknownResource(path, "");
    }

    public Object getUnknownResource(String path, String category) {
        HashMap<String, ResourceLoader<?>> loaders = category.equals("") ? extensionLoaders : extensionCategories.get(category);
        if(loaders == null) {
            throw new RuntimeException("Unknown resource category: " + category);
        }
        classLoader.getResource(path);

        return null;
    }

    public void saveResource(Object resource, String path) {
        String extension = getExtension(path);
        if(extension == null) {
            throw new RuntimeException("The provided path does not have an extension");
        }
        ResourceLoader<?> resourceLoader = extensionLoaders.get(extension);
        if(resourceLoader == null) {
            throw new RuntimeException("No Resource Loader found for extension: " + extension);
        }
        resourceLoader.write(resource, path);
    }

    public ArrayList<ResourceLoader<?>> getCategory(String category) {
        return categories.computeIfAbsent(category, a -> new ArrayList<>());
    }

    public HashMap<String, ResourceLoader<?>> getExtensionCategory(String category) {
        return extensionCategories.computeIfAbsent(category, a -> new HashMap<>());
    }

    @Nullable
    public static String getExtension(String path) {
        int pos;
        for(pos = path.length() - 1; pos > 0; pos--) {
            if(path.charAt(pos) == '.') {
                return path.substring(pos + 1);
            }
        }
        return null;
    }
}
