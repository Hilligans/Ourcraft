package dev.hilligans.ourcraft.client.rendering.newrenderer;

import dev.hilligans.ourcraft.client.rendering.world.managers.TextureManager;
import org.joml.Vector3f;

public interface IModel {

    float[] getVertices(int side);
    int[] getIndices(int side);
    default float[] getVertices(int side, int rotX, int rotY) {
        return getVertices(side);
    }

    default int[] getIndices(int side, int rotX, int rotY) {
        return getIndices(side);
    }
    void addData(PrimitiveBuilder primitiveBuilder, TextureManager textureManager, int side, float size, Vector3f offset, int rotX, int rotY);

    String getModel();
    String getPath();


}
