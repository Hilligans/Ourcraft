package dev.hilligans.engine.resource.dataloader;

import dev.hilligans.engine.resource.IBufferAllocator;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public interface ResourceDirectory {

    ByteBuffer get(String path) throws IOException;

    ByteBuffer getDirect(String path) throws IOException;

    ByteBuffer get(String path, IBufferAllocator allocator) throws IOException;

    List<String> getFiles(String path);

    List<String> getValidEnding(String path, ArrayList<String> fileEndings);
}
