package dev.hilligans.ourcraft.world.gen;

import dev.hilligans.engine.data.BoundingBox;

public interface IWorldHeightBuilder {


    int getWorldHeight(long x, long y, long z);

    IWorldHeightBuilder setSeed(long seed);

    /**
     * Returns plane pairs for how to generate terrain.
     * Anything with PlaneType.SURFACE will ask to get the surface height with an x y z variable as a point on the plane. The returned value will be the height offset from the plane.
     */
    default GenerationBoundingBox getGenerationBoundingBox() {
        return new GenerationBoundingBox(Integer.MIN_VALUE, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, 64, Integer.MAX_VALUE, new PlaneType[]{
            null, PlaneType.BEDROCK, null, null, PlaneType.SURFACE, null
        });
    }


    class GenerationBoundingBox extends BoundingBox {

        final PlaneType[] planeType;

        /**
         * @param planeType positive x, positive y, positive z, negative x, negative y, negative z
         */
        public GenerationBoundingBox(long x1, long y1, long z1, long x2, long y2, long z2, PlaneType[] planeType) {
            super(x1, y1, z1, x2, y2, z2);
            this.planeType = planeType;
        }
    }

    enum PlaneType {

        SURFACE,
        BEDROCK
    }
}
