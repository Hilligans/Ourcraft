package dev.hilligans.ourcraft.Resource.DataLoader;

import dev.hilligans.ourcraft.Resource.IBufferAllocator;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public interface ResourceDirectory {

    ByteBuffer get(String path) throws IOException;

    ByteBuffer getDirect(String path) throws IOException;

    ByteBuffer get(String path, IBufferAllocator allocator) throws IOException;

    ArrayList<String> getFiles(String path);

    ArrayList<String> getValidEnding(String path, ArrayList<String> fileEndings);
}
