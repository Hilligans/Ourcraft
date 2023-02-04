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
        long a = 0;
        long b = 0;
        if (x == radius) {
            a = y;
            b = z;
        } else if (y == radius) {
            a = x;
            b = z;
        } else {
            a = x;
            b = y;
        }

        //return (int) Math.sqrt((long) radius *radius*3 - a*a - b*b);

        return random.nextInt(4);
    }

    @Override
    public GenerationBoundingBox getGenerationBoundingBox() {
        return new GenerationBoundingBox(-radius, -radius, -radius, radius, radius, radius, new PlaneType[]{
                PlaneType.SURFACE, PlaneType.SURFACE, PlaneType.SURFACE, PlaneType.SURFACE, PlaneType.SURFACE, PlaneType.SURFACE
        });
    }
}
