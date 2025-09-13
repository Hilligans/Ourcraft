package dev.hilligans.engine.resource;

import org.lwjgl.system.MemoryUtil;

import java.nio.Buffer;
import java.nio.ByteBuffer;

public class BufferAllocator implements IBufferAllocator {

    @Override
    public ByteBuffer malloc(int size) {
        return MemoryUtil.memAlloc(size);
    }

    @Override
    public ByteBuffer calloc(int size) {
        return MemoryUtil.memCalloc(size);
    }

    @Override
    public void free(Buffer buffer) {
        MemoryUtil.memFree(buffer);
    }
}
