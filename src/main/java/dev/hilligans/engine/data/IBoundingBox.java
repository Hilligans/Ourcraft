package dev.hilligans.engine.data;

import org.joml.Intersectionf;
import org.joml.Vector2f;

public interface IBoundingBox {



    float minX();
    float minY();
    float minZ();
    float maxX();
    float maxY();
    float maxZ();

    default boolean intersects(IBoundingBox boundingBox) {
        return  (maxX() > boundingBox.minX() && minX() < boundingBox.maxX()) &&
                (maxY() > boundingBox.minY() && minY() < boundingBox.maxY()) &&
                (maxZ() > boundingBox.minZ() && minZ() < boundingBox.maxZ());
    }

    default boolean intersectsX(IBoundingBox boundingBox) {
        return maxX() > boundingBox.minX() && minX() < boundingBox.maxX();
    }

    default boolean intersectsY(IBoundingBox boundingBox) {
        return maxY() > boundingBox.minY() && minY() < boundingBox.maxY();
    }

    default boolean intersectsZ(IBoundingBox boundingBox) {
        return maxZ() > boundingBox.minZ() && minZ() < boundingBox.maxZ();
    }

    default boolean intersects(IBoundingBox boundingBox, double sourceX, double sourceY, double sourceZ, double targetX, double targetY, double targetZ) {
        return this.minX() + sourceX <= boundingBox.maxX() + targetX && this.minY() + sourceY <= boundingBox.maxY() + targetY && this.minZ() + sourceZ <= boundingBox.maxZ() + targetZ &&
                this.maxX() + sourceX >= boundingBox.minX() + targetX && this.maxY() + sourceY >= boundingBox.minY() + targetY && this.maxZ() + sourceZ >= boundingBox.minZ() + targetZ;
    }

    default float intersectsRay(float x, float y, float z, float dirX, float dirY, float dirZ, Vector2f vector2f) {
        if (!Intersectionf.intersectRayAab(x, y, z, dirX, dirY, dirZ, minX(), minY(), minZ(), maxX(), maxY(), maxZ(), vector2f)) {
            return -1;
        }
        return vector2f.x;
    }

    public static final IBoundingBox EMPTY_BOX = new IBoundingBox() {
        @Override
        public float minX() {
            return 0;
        }

        @Override
        public float minY() {
            return 0;
        }

        @Override
        public float minZ() {
            return 0;
        }

        @Override
        public float maxX() {
            return 0;
        }

        @Override
        public float maxY() {
            return 0;
        }

        @Override
        public float maxZ() {
            return 0;
        }
    };
}
