package dev.hilligans.engine.resource;

import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;

public class StackBufferAllocator implements IBufferAllocator {

    public MemoryStack memoryStack;

    public StackBufferAllocator(MemoryStack memoryStack) {
        this.memoryStack = memoryStack;
    }

    @Override
    public ByteBuffer malloc(int size) {
        return memoryStack.malloc(size);
    }

    @Override
    public ByteBuffer calloc(int size) {
        return memoryStack.calloc(size);
    }
}
