package dev.hilligans.engine.resource;

import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;

public class HeapAllocator implements IBufferAllocator {

    public static final HeapAllocator INSTANCE = new HeapAllocator();

    @Override
    public ByteBuffer malloc(int size) {
        return BufferUtils.createByteBuffer(size);
    }

    @Override
    public ByteBuffer calloc(int size) {
        return BufferUtils.createByteBuffer(size);
    }
}
