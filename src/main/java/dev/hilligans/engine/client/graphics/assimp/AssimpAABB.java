package dev.hilligans.engine.client.graphics.assimp;

import dev.hilligans.engine.data.IBoundingBox;
import org.lwjgl.assimp.AIAABB;

public class AssimpAABB implements IBoundingBox {

    AIAABB aiaabb;

    public AssimpAABB(AIAABB aiaabb) {
        this.aiaabb = aiaabb;
    }

    @Override
    public float minX() {
        return aiaabb.mMin().x();
    }

    @Override
    public float minY() {
        return aiaabb.mMin().y();
    }

    @Override
    public float minZ() {
        return aiaabb.mMin().z();
    }

    @Override
    public float maxX() {
        return aiaabb.mMax().x();
    }

    @Override
    public float maxY() {
        return aiaabb.mMax().y();
    }

    @Override
    public float maxZ() {
        return aiaabb.mMax().z();
    }

    @Override
    public String toString() {
        return "AssimpAABB{" +
                "minX=" + minX() +
                ", minY=" + minY() +
                ", minZ=" + minZ() +
                ", maxX=" + maxX() +
                ", maxY=" + maxY() +
                ", maxZ=" + maxZ() +
                "}";
    }
}
