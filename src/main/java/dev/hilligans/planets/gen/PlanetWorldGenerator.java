package dev.hilligans.planets.gen;

import dev.hilligans.ourcraft.world.newworldsystem.*;
import dev.hilligans.ourcraft.world.gen.IWorldHeightBuilder;

public class PlanetWorldGenerator implements IWorldGenerator {

    public long seed;
    public IWorld world;
    public IThreeDChunkContainer chunkContainer;
    public PlanetWorldHeightBuilder heightBuilder;

    public int radius;
    public int minHeightFromRadius;
    public int maxHeightFromRadius;

    public PlanetWorldGenerator(int radius) {
        this(radius, 30, 30, 0);
    }

    public PlanetWorldGenerator(int radius, int minHeightFromRadius, int maxHeightFromRadius, long seed) {
        this.radius = radius;
        this.minHeightFromRadius = minHeightFromRadius;
        this.maxHeightFromRadius = maxHeightFromRadius;
        heightBuilder = new PlanetWorldHeightBuilder(new IWorldHeightBuilder[]{
                new SimpleHeightBuilder(),
                new SimpleHeightBuilder(),
                new SimpleHeightBuilder(),
                new SimpleHeightBuilder(),
                new SimpleHeightBuilder(),
                new SimpleHeightBuilder()
        }, 64);
        setSeed(seed);
    }

    @Override
    public void setSeed(long seed) {
        this.seed = seed;
        heightBuilder.setSeed(seed);
    }

    @Override
    public void setWorld(IWorld world, IThreeDChunkContainer chunkContainer) {
        this.world = world;
        this.chunkContainer = chunkContainer;
    }

    @Override
    public void generateChunk(IChunk chunk, IWorld world) {

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
        return 32;
    }

    @Override
    public int structureMinimumDistance() {
        return 0;
    }
}
