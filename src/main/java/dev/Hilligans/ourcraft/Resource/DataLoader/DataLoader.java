package dev.Hilligans.ourcraft.Resource.DataLoader;

import dev.Hilligans.ourcraft.Resource.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.zip.ZipFile;

public class DataLoader {

    public HashMap<String, ResourceDirectory> resourceDirectoryHashMap = new HashMap<>();

    public void add(String modID, ResourceDirectory resourceDirectory) {
        resourceDirectoryHashMap.put(modID, resourceDirectory);
    }

    public void remove(String modID) {
        resourceDirectoryHashMap.remove(modID);
    }

    public void addFolder(String path, String modID) {
        add(modID, new FolderResourceDirectory(new File(path)));
    }

    public void addJar(String path, String modID) {
        try {
            add(modID, new ZipResourceDirectory(new ZipFile(path), path));
        } catch (Exception e) {
            throw new RuntimeException("Failed to find jar file: " + path);
        }
    }

    @Nullable
    public ByteBuffer get(ResourceLocation resourceLocation) {
        ResourceDirectory resourceDirectory = resourceDirectoryHashMap.get(resourceLocation.getSource());
        if(resourceDirectory == null) {
            return null;
        }
        try {
            return resourceDirectory.get(resourceLocation.path);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Nullable
    public ByteBuffer getDirect(ResourceLocation resourceLocation) {
        ResourceDirectory resourceDirectory = resourceDirectoryHashMap.get(resourceLocation.getSource());
        if(resourceDirectory == null) {
            return null;
        }
        try {
            return resourceDirectory.getDirect(resourceLocation.path);
        } catch (IOException e) {
            return null;
        }
    }
}
