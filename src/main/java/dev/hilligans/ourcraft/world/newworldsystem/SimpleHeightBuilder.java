package dev.hilligans.ourcraft.world.newworldsystem;

import dev.hilligans.ourcraft.util.noises.PerlinNoise;
import dev.hilligans.ourcraft.world.gen.IWorldHeightBuilder;

public class SimpleHeightBuilder implements IWorldHeightBuilder {

    public int surfaceHeight = 64;
    public long seed;
    PerlinNoise perlinNoise = new PerlinNoise(0, 0.5, 0.03, 40, 3);

    @Override
    public int getWorldHeight(long x, long y, long z) {
        return (int) perlinNoise.getHeight(x, z) + 10;
    }

    @Override
    public IWorldHeightBuilder setSeed(long seed) {
        this.seed = seed;
        perlinNoise.setSeed((short) seed);
        return this;
    }

    @Override
    public GenerationBoundingBox getGenerationBoundingBox() {
        return new GenerationBoundingBox(Integer.MIN_VALUE, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, surfaceHeight, Integer.MAX_VALUE, new PlaneType[]{
                null, PlaneType.BEDROCK, null, null, PlaneType.SURFACE, null
        });
    }
}
