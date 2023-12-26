package dev.hilligans.ourcraft.world.newworldsystem;

import dev.hilligans.ourcraft.data.other.ChunkPos;

import java.util.function.Consumer;

public interface IThreeDContainer<T> {

    T getChunk(long x, long y, long z);

    default T getChunk(ChunkPos chunkPos) {
        return getChunk(chunkPos.chunkX, chunkPos.chunkY, chunkPos.chunkZ);
    }

    T setChunk(long x, long y, long z, T val);

    T removeChunk(long x, long y, long z);

    T removeChunk(long pos);

    int getSize();

    void forEach(Consumer<T> consumer);

    void clear();
}
