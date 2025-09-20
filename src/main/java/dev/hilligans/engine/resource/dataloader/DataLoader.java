package dev.hilligans.engine.resource.dataloader;

import dev.hilligans.engine.GameInstance;
import dev.hilligans.engine.resource.IBufferAllocator;
import dev.hilligans.engine.resource.ResourceLocation;
import dev.hilligans.engine.util.argument.Argument;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.zip.ZipFile;

public class DataLoader {

    public static final Argument<Boolean> strictLoading = Argument.existArg("--strictLoading")
            .help("Forces the engine to only load resources that match its modID, in weird packaging situations, this should remain disabled");

    public HashMap<String, ResourceDirectory> resourceDirectoryHashMap = new HashMap<>();
    public ArrayList<ResourceDirectory> allResourceDirectories = new ArrayList<>();

    public GameInstance gameInstance;

    public DataLoader(GameInstance gameInstance) {
        this.gameInstance = gameInstance;
    }

    public void add(String modID, ResourceDirectory resourceDirectory) {
        this.resourceDirectoryHashMap.put(modID, resourceDirectory);
        this.allResourceDirectories.add(resourceDirectory);
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

    public void addStream() {
        add("", new StreamResourceDirectory());
    }

    @Nullable
    public ByteBuffer get(ResourceLocation resourceLocation) {
        ResourceDirectory resourceDirectory = resourceDirectoryHashMap.get(resourceLocation.getSource());
        if(resourceDirectory == null) {
            if(strictLoading.get(gameInstance)) {
                return null;
            }

            for(ResourceDirectory directory : allResourceDirectories) {
                try {
                    return directory.get(resourceLocation.path);
                } catch (IOException _) {}
            }
            return null;
        }
        try {
            return resourceDirectory.get(resourceLocation.path);
        } catch (IOException e) {
            return null;
        }
    }

    @Nullable
    public ByteBuffer getDirect(ResourceLocation resourceLocation) {
        ResourceDirectory resourceDirectory = resourceDirectoryHashMap.get(resourceLocation.getSource());
        if(resourceDirectory == null) {
            if(strictLoading.get(gameInstance)) {
                return null;
            }

            for(ResourceDirectory directory : allResourceDirectories) {
                try {
                    return directory.getDirect(resourceLocation.path);
                } catch (IOException _) {}
            }
            return null;
        }
        try {
            return resourceDirectory.getDirect(resourceLocation.path);
        } catch (IOException e) {
            return null;
        }
    }

    public ByteBuffer get(ResourceLocation resourceLocation, IBufferAllocator allocator) {
        ResourceDirectory resourceDirectory = resourceDirectoryHashMap.get(resourceLocation.getSource());
        if(resourceDirectory == null) {
            if(strictLoading.get(gameInstance)) {
                return null;
            }

            for(ResourceDirectory directory : allResourceDirectories) {
                try {
                    return directory.get(resourceLocation.path, allocator);
                } catch (IOException _) {}
            }
            return null;
        }
        try {
            return resourceDirectory.get(resourceLocation.path, allocator);
        } catch (IOException e) {
            return null;
        }
    }
}
