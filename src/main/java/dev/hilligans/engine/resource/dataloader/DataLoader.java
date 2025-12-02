package dev.hilligans.engine.resource.dataloader;

import dev.hilligans.engine.Engine;
import dev.hilligans.engine.GameInstance;
import dev.hilligans.engine.resource.IBufferAllocator;
import dev.hilligans.engine.resource.ResourceLocation;
import dev.hilligans.engine.util.Logger;
import dev.hilligans.engine.util.argument.Argument;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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
        if(Engine.verbose.get()) {
            System.out.println("Looking for resource: \"" + resourceLocation.identifier() + "\"");
        }

        ResourceDirectory resourceDirectory = resourceDirectoryHashMap.get(resourceLocation.getSource());
        if(resourceDirectory == null) {
            if(strictLoading.get(gameInstance)) {
                return null;
            }

            for(ResourceDirectory directory : allResourceDirectories) {
                try {
                    ByteBuffer buffer = directory.get(resourceLocation.path);
                    if(buffer == null) {
                        continue;
                    }
                    if(Engine.verbose.get()) {
                        System.out.println("Found resource: \"" + resourceLocation.identifier() + "\" in " + directory.getName());
                    }
                    return buffer;
                } catch (IOException _) {}
            }

            if(Engine.verbose.get()) {
                System.out.println("Failed to find resource: \"" + resourceLocation.identifier() + "\"");
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
                    ByteBuffer buffer = directory.getDirect(resourceLocation.path);
                    if(buffer == null) {
                        continue;
                    }
                    return buffer;
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
                    ByteBuffer buffer = directory.get(resourceLocation.path, allocator);
                    if(buffer == null) {
                        continue;
                    }
                    return buffer;
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

    public void forEach(String path, Consumer<String> consumer) {
        HashSet<String> fileSet = new HashSet<>();

        for(ResourceDirectory resourceDirectory : allResourceDirectories) {
            List<String> resources = resourceDirectory.getFiles(path);

            //System.out.println(resourceDirectory + ":" + resources + ":" + path);

            for(String s : resources) {
                if(!fileSet.contains(s)) {
                    fileSet.add(s);
                    consumer.accept(s);
                }
            }
        }
    }
}
