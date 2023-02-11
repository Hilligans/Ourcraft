package dev.hilligans.ourcraft.World.NewWorldSystem;

import dev.hilligans.ourcraft.Util.Noises.PerlinNoise;
import dev.hilligans.ourcraft.World.WorldGen.IWorldHeightBuilder;
import org.joml.Random;
import org.joml.SimplexNoise;

public class SimpleHeightBuilder implements IWorldHeightBuilder {

    public int surfaceHeight = 64;
    PerlinNoise perlinNoise = new PerlinNoise(new Random().nextInt(Short.MAX_VALUE), 0.5, 0.03, 40, 3);

    @Override
    public int getWorldHeight(long x, long y, long z) {
        return (int) perlinNoise.getHeight(x, z) + 10;
    }

    @Override
    public GenerationBoundingBox getGenerationBoundingBox() {
        return new GenerationBoundingBox(Integer.MIN_VALUE, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, surfaceHeight, Integer.MAX_VALUE, new PlaneType[]{
                null, PlaneType.BEDROCK, null, null, PlaneType.SURFACE, null
        });
    }
}
