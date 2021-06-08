package Hilligans.Client.Rendering.NewRenderer;

import Hilligans.Client.Rendering.World.Managers.BlockTextureManager;
import Hilligans.Client.Rendering.World.Managers.TextureManager;
import Hilligans.Data.Other.BlockPos;
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

}
