package dev.hilligans.engine.resource;

import java.lang.foreign.MemorySegment;
import java.nio.Buffer;
import java.nio.ByteBuffer;

public interface IBufferAllocator {

    ByteBuffer malloc(int size);

    ByteBuffer calloc(int size);

    default MemorySegment mallocSegment(long size) { return null;}
    default MemorySegment callocSegment(long size) { return null;}


    default void free(Buffer buffer) {}

    default void free(MemorySegment segment) {}
}
