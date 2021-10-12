package dev.Hilligans.Client.Rendering.NewRenderer;

import dev.Hilligans.Client.Rendering.World.Managers.TextureManager;
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
