package dev.hilligans.ourcraft.World.NewWorldSystem;

public class PreTickedWorld implements IWorld {


    @Override
    public String getName() {
        return null;
    }

    @Override
    public int getID() {
        return 0;
    }

    @Override
    public void tick() {

    }

    @Override
    public IChunk getChunk(long blockX, long blockY, long blockZ) {
        return null;
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
