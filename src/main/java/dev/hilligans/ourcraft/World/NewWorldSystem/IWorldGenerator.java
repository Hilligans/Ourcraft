package dev.hilligans.ourcraft.World.NewWorldSystem;

public interface IWorldGenerator {

    void setSeed(long seed);

    void setWorld(IWorld world, IThreeDChunkContainer chunkContainer);

    void generateChunk(IChunk chunk, IWorld world);

    boolean isPregenerated();

    boolean supports3DChunks();

    int featureMinimumDistance();

    int structureMinimumDistance();
}
