package dev.hilligans.planets.gen;

import dev.hilligans.ourcraft.world.gen.IWorldHeightBuilder;
import org.joml.Math;

import java.util.Random;

public class PlanetWorldHeightBuilder implements IWorldHeightBuilder {

    public IWorldHeightBuilder[] builders;

    public int radius;
    public long seed;
    public int warpingRadius;
    public float w = 20;


    public PlanetWorldHeightBuilder(IWorldHeightBuilder[] builders, int radius) {
        this.builders = builders;
        this.radius = radius;
        this.warpingRadius = (int) (radius - w);
    }

    @Override
    public int getWorldHeight(long x, long y, long z) {
        double absX = Math.abs(x);
        double absY = Math.abs(y);
        double absZ = Math.abs(z);
        int comp;
        if (absX >= absY && absX >= absZ) {
            comp = x > 0 ? 1 : -1;
        } else if (absY >= absZ) {
            comp = y > 0 ? 2 : -2;
        } else {
            comp = z > 0 ? 3 : -3;
        }

        if(comp == 1) {
            int val = builders[0].getWorldHeight(y, radius, z);
            float count = 1;
            if(y > warpingRadius) {
                float effect = getEffect(y);
                count += effect;
                val += builders[1].getWorldHeight(y, radius, z) * effect;
            } else if(y < -warpingRadius) {
                float effect = getEffect(-y);
                count += effect;
                val += builders[4].getWorldHeight(-y, radius, z) * effect;
            }
            if(z > warpingRadius) {
                float effect = getEffect(z);
                count += effect;
                val += builders[2].getWorldHeight(z, radius, y) * effect;
            } else if(z < -warpingRadius) {
                float effect = getEffect(-z);
                count += effect;
                val += builders[5].getWorldHeight(-z, radius, y) * effect;
            }
            return (int) (val / count);
        } else if (comp == 2) {
            int val = builders[1].getWorldHeight(x, radius, z);
            float count = 1;
            if(x > warpingRadius) {
                float effect = getEffect(x);
                count += effect;
                val += builders[0].getWorldHeight(x, radius, z) * effect;
            } else if(x < -warpingRadius) {
                float effect = getEffect(-x);
                count += effect;
                val += builders[3].getWorldHeight(-x, radius, z) * effect;
            }
            if(z > warpingRadius) {
                float effect = getEffect(z);
                count += effect;
                val += builders[2].getWorldHeight(x, radius, z) * effect;
            } else if(z < -warpingRadius) {
                float effect = getEffect(-z);
                count += effect;
                val += builders[5].getWorldHeight(x, radius, -z) * effect;
            }
            return (int) (val / count);
        } else if(comp == 3) {
            int val = builders[2].getWorldHeight(x, radius, y);
            float count = 1;
            if(y > warpingRadius) {
                float effect = getEffect(y);
                count += effect;
                val += builders[1].getWorldHeight(x, radius, y) * effect;
            } else if(y < -warpingRadius) {
                float effect = getEffect(-y);
                count += effect;
                val += builders[4].getWorldHeight(x, radius, -y) * effect;
            }
            if(x > warpingRadius) {
                float effect = getEffect(x);
                count += effect;
                val += builders[0].getWorldHeight(y, radius, x) * effect;
            } else if(x < -warpingRadius) {
                float effect = getEffect(-x);
                count += effect;
                val += builders[3].getWorldHeight(y, radius, -x) * effect;
            }
            return (int) (val / count);
        } else if(comp == -1) {
            int val = builders[3].getWorldHeight(y, radius, z);
            float count = 1;
            if(y > warpingRadius) {
                float effect = getEffect(y);
                count += effect;
                val += builders[1].getWorldHeight(-y, radius, z) * effect;
            } else if(y < -warpingRadius) {
                float effect = getEffect(-y);
                count += effect;
                val += builders[4].getWorldHeight(y, radius, z) * effect;
            }
            if(z > warpingRadius) {
                float effect = getEffect(z);
                count += effect;
                val += builders[2].getWorldHeight(-z, radius, y) * effect;
            } else if(z < -warpingRadius) {
                float effect = getEffect(-z);
                count += effect;
                val += builders[5].getWorldHeight(z, radius, y) * effect;
            }
            return (int) (val / count);
        } else if(comp == -2) {
            int val = builders[4].getWorldHeight(x, radius, z);
            float count = 1;
            if(x > warpingRadius) {
                float effect = getEffect(x);
                count += effect;
                val += builders[0].getWorldHeight(-x, radius, z) * effect;
            } else if(x < -warpingRadius) {
                float effect = getEffect(-x);
                count += effect;
                val += builders[3].getWorldHeight(x, radius, z) * effect;
            }
            if(z > warpingRadius) {
                float effect = getEffect(z);
                count += effect;
                val += builders[2].getWorldHeight(x, radius, -z) * effect;
            } else if(z < -warpingRadius) {
                float effect = getEffect(-z);
                count += effect;
                val += builders[5].getWorldHeight(x, radius, z) * effect;
            }
            return (int) (val / count);
        } else if(comp == -3) {
            int val = builders[5].getWorldHeight(x, radius, y);
            float count = 1;
            if(y > warpingRadius) {
                float effect = getEffect(y);
                count += effect;
                val += builders[1].getWorldHeight(x, radius, -y) * effect;
            } else if(y < -warpingRadius) {
                float effect = getEffect(-y);
                count += effect;
                val += builders[4].getWorldHeight(x, radius, y) * effect;
            }
            if(x > warpingRadius) {
                float effect = getEffect(x);
                count += effect;
                val += builders[0].getWorldHeight(y, radius, -x) * effect;
            } else if(x < -warpingRadius) {
                float effect = getEffect(-x);
                count += effect;
                val += builders[3].getWorldHeight(y, radius, x) * effect;
            }
            return (int) (val / count);
        } else {
            return 0;
        }
    }

    @Override
    public IWorldHeightBuilder setSeed(long seed) {
        this.seed = seed;
        Random random = new Random(seed);
        for(IWorldHeightBuilder heightBuilder : builders) {
            heightBuilder.setSeed(random.nextLong());
        }
        return this;
    }

    public float getEffect(long distance) {
        float val = (distance - warpingRadius) / w;
        val = val > 1 ? 1 : val;
        return val;
    }

    @Override
    public GenerationBoundingBox getGenerationBoundingBox() {
        return new GenerationBoundingBox(-radius, -radius, -radius, radius, radius, radius, new PlaneType[]{
                PlaneType.SURFACE, PlaneType.SURFACE, PlaneType.SURFACE, PlaneType.SURFACE, PlaneType.SURFACE, PlaneType.SURFACE
        });
    }
}
