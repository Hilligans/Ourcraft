package dev.hilligans.ourcraft.World.NewWorldSystem;

import dev.hilligans.ourcraft.World.Chunk;

import java.util.function.Consumer;

public interface IChunkContainer {

    Chunk getChunk(int x, int z);
    Chunk setChunk(int x, int z, Chunk chunk);
    Chunk removeChunk(int x, int z);
    Chunk removeChunk(long pos);
    int getSize();
    void forEach(Consumer<Chunk> consumer);

}
