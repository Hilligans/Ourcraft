package dev.hilligans.planets.gen;

import dev.hilligans.ourcraft.World.WorldGen.IWorldHeightBuilder;

import java.util.Random;

public class PlanetWorldHeightBuilder implements IWorldHeightBuilder {

    public IWorldHeightBuilder builder;

    public int radius;
    public Random random;


    public PlanetWorldHeightBuilder(IWorldHeightBuilder builder, int radius) {
        this.builder = builder;
        this.radius = radius;
        this.random = new Random();
    }

    @Override
    public int getWorldHeight(long x, long y, long z) {
        if (x == radius) {

        } else if (y == radius) {

        } else {

        }
        return random.nextInt(4);
    }

    @Override
    public GenerationBoundingBox getGenerationBoundingBox() {
        return new GenerationBoundingBox(-radius, -radius, -radius, radius, radius, radius, new PlaneType[]{
                PlaneType.SURFACE, PlaneType.SURFACE, PlaneType.SURFACE, PlaneType.SURFACE, PlaneType.SURFACE, PlaneType.SURFACE
        });
    }
}
