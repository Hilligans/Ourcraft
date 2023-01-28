package dev.hilligans.ourcraft.World.NewWorldSystem;

public class NewClientWorld implements IWorld {

    public String name;
    public int worldID;
    IThreeDChunkContainer chunkContainer = new NewChunkContainer();


    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getID() {
        return worldID;
    }

    @Override
    public void tick() {

    }

    @Override
    public IChunk getChunk(long blockX, long blockY, long blockZ) {
        return chunkContainer.getChunk(blockX,blockY,blockZ);
    }

    @Override
    public IChunk getChunkNonNull(long blockX, long blockY, long blockZ) {
        return null;
    }

    @Override
    public void setChunk(long blockX, long blockY, long blockZ, IChunk chunk) {

    }

    @Override
    public IThreeDChunkContainer getChunkContainer() {
        return null;
    }
}
