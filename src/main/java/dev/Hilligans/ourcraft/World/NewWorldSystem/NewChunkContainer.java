package dev.Hilligans.ourcraft.World.NewWorldSystem;

import java.util.function.Consumer;

public class NewChunkContainer implements IThreeDChunkContainer {


    @Override
    public IChunk getChunk(long x, long y, long z) {
        return null;
    }

    @Override
    public IChunk setChunk(long x, long y, long z, IChunk chunk) {
        return null;
    }

    @Override
    public IChunk removeChunk(long x, long y, long z) {
        return null;
    }

    @Override
    public IChunk removeChunk(long pos) {
        return null;
    }

    @Override
    public int getSize() {
        return 0;
    }

    @Override
    public void forEach(Consumer<IChunk> consumer) {

    }

    @Override
    public int getChunkWidth() {
        return 0;
    }

    @Override
    public int getChunkHeight() {
        return 0;
    }
}
