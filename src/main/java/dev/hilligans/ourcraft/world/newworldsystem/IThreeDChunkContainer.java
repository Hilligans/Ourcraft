package dev.hilligans.ourcraft.world.newworldsystem;

import java.util.function.Consumer;

public interface IThreeDChunkContainer extends IThreeDContainer<IChunk> {

    IChunk getChunk(long x, long y, long z);

    IChunk setChunk(long x, long y, long z, IChunk chunk);

    IChunk removeChunk(long x, long y, long z);

    IChunk removeChunk(long pos);

    int getSize();

    void forEach(Consumer<IChunk> consumer);

    int getChunkWidth();

    int getChunkHeight();

}
