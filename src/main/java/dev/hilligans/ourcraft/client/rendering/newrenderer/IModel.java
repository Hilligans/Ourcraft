package dev.hilligans.ourcraft.client.rendering.newrenderer;

import dev.hilligans.ourcraft.client.rendering.world.managers.BlockTextureManager;
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
    void addData(TextAtlas textAtlas, PrimitiveBuilder primitiveBuilder, BlockTextureManager textureManager, int side, float size, Vector3f offset, int rotX, int rotY);

    String getModel();
    String getPath();


}
