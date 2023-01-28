package dev.hilligans.ourcraft.World.NewWorldSystem;

import dev.hilligans.ourcraft.Block.Blocks;

public class SimpleChunkGenerator implements IWorldGenerator {

    public int chunkWidth;
    public int chunkHeight;

    boolean threeDimensionChunks;

    public IWorld world;
    public IThreeDChunkContainer chunkContainer;

    public int terrainMin = 64;

    long seed;

    @Override
    public void setSeed(long seed) {
        this.seed = seed;
    }

    @Override
    public void setWorld(IWorld world, IThreeDChunkContainer chunkContainer) {
        this.world = world;
        this.chunkContainer = chunkContainer;
    }

    @Override
    public boolean isPregenerated() {
        return false;
    }

    @Override
    public boolean supports3DChunks() {
        return true;
    }

    @Override
    public int featureMinimumDistance() {
        return Math.max(chunkWidth,chunkHeight);
    }

    @Override
    public int structureMinimumDistance() {
        return 0;
    }

    @Override
    public void generateChunk(IChunk chunk, IWorld world) {
        if(!chunk.isGenerated()) {
            //TODO change to use a fast fill
            for(long y = chunk.getChunkYBlockPos(); y < terrainMin; y++) {
                for(int x = 0; x < chunk.getWidth(); x++) {
                    for(int z = 0; z < chunk.getWidth(); z++) {
                        chunk.setBlockState(x,y,z, Blocks.STONE.getDefaultState1());
                    }
                }
            }
        }
    }
}
