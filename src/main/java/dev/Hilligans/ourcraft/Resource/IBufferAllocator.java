package dev.Hilligans.ourcraft.Resource;

import org.lwjgl.system.MemoryStack;

import java.nio.Buffer;
import java.nio.ByteBuffer;

public interface IBufferAllocator {

    ByteBuffer malloc(int size);

    ByteBuffer calloc(int size);

    default void free(Buffer buffer) {}
}
