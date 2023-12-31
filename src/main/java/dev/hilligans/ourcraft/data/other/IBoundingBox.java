package dev.hilligans.ourcraft.data.other;

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

    float intersectsRay(float x, float y, float z, float dirX, float dirY, float dirZ, Vector2f vector2f);
}
