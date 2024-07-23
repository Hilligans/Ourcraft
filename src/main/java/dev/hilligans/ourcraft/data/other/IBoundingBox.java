package dev.hilligans.ourcraft.data.other;

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
