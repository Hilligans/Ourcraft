package dev.hilligans.ourcraft.data.other;

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
}
