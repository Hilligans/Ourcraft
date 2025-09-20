package dev.hilligans.engine.resource.dataloader;

import dev.hilligans.engine.resource.IBufferAllocator;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class StreamResourceDirectory implements ResourceDirectory {

    @Override
    public ByteBuffer get(String path) throws IOException {
        try(InputStream stream = StreamResourceDirectory.class.getResourceAsStream("/" + path)) {
            if(stream != null) {
                ByteBuffer byteBuffer = ByteBuffer.allocate(stream.available());
                byteBuffer.put(stream.readAllBytes()).flip();
                return byteBuffer;
            }
        }
        return null;
    }

    @Override
    public ByteBuffer getDirect(String path) throws IOException {
        try(InputStream stream = StreamResourceDirectory.class.getResourceAsStream("/" + path)) {
            if(stream != null) {
                ByteBuffer byteBuffer = ByteBuffer.allocateDirect(stream.available());
                byteBuffer.put(stream.readAllBytes()).flip();
                return byteBuffer;
            }
        }
        return null;
    }

    @Override
    public ByteBuffer get(String path, IBufferAllocator allocator) throws IOException {
        try(InputStream stream = StreamResourceDirectory.class.getResourceAsStream("/" + path)) {
            if(stream != null) {
                ByteBuffer byteBuffer = allocator.malloc(stream.available());
                byteBuffer.put(stream.readAllBytes()).flip();
                return byteBuffer;
            }
        }
        return null;
    }

    @Override
    public ArrayList<String> getFiles(String path) {
        return new ArrayList<>();
    }

    @Override
    public ArrayList<String> getValidEnding(String path, ArrayList<String> fileEndings) {
        return new ArrayList<>();
    }
}
