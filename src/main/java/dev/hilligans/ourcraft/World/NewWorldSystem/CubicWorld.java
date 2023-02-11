package dev.hilligans.ourcraft.World.NewWorldSystem;

import org.joml.Vector3f;
import org.joml.Vector3fc;

public class CubicWorld implements IWorld {

    public int radius;
    public String worldName;
    public int id;
    public final IThreeDChunkContainer chunkContainer = new CubicChunkContainer(32, 32);

    public CubicWorld(int id, String worldName, int radius) {
        this.radius = radius;
        this.worldName = worldName;
        this.id = id;
    }

    @Override
    public String getName() {
        return worldName;
    }

    @Override
    public int getID() {
        return id;
    }

    @Override
    public void tick() {
    }

    //@Override
    //public Vector3fc getGravityVector(Vector3f position) {
      //  int component = position.maxComponent();
      //  return new Vector3f().setComponent(component, position.get(component) > 0 ? -1 : 1);
        //}

    @Override
    public IChunk getChunk(long blockX, long blockY, long blockZ) {
        return chunkContainer.getChunk(blockX >> 5, blockY >> 5, blockZ >> 5);
    }

    @Override
    public IChunk getChunkNonNull(long blockX, long blockY, long blockZ) {
        return getChunk(blockX, blockY, blockZ);
    }

    @Override
    public void setChunk(long blockX, long blockY, long blockZ, IChunk chunk) {
        chunkContainer.setChunk(blockX >> 5, blockY >> 5, blockZ >> 5, chunk);
        chunk.setDirty(true);
    }

    @Override
    public IThreeDChunkContainer getChunkContainer() {
        return chunkContainer;
    }
}
