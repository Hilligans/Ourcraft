package dev.hilligans.ourcraft.world.newworldsystem;

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
