package dev.Hilligans.ourcraft.World.ThreeDimension;

import dev.Hilligans.ourcraft.World.Chunk;
import dev.Hilligans.ourcraft.World.NewChunk;

import java.util.function.Consumer;

public interface IThreeDChunkContainer {

    NewChunk getChunk(int x, int y, int z);
    NewChunk setChunk(int x, int y, int z, NewChunk chunk);
    NewChunk removeChunk(int x, int y, int z);
    NewChunk removeChunk(long pos);
    int getSize();
    void forEach(Consumer<NewChunk> consumer);

}
