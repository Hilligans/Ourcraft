package dev.Hilligans.ourcraft.World.NewWorldSystem;

import java.util.function.Consumer;

public class NewChunkContainer extends EmptyContainer<IChunk> implements IThreeDChunkContainer {

    @Override
    public int getChunkWidth() {
        return 16;
    }

    @Override
    public int getChunkHeight() {
        return 256;
    }
}
