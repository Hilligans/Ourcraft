package Hilligans.Data.Other.BlockShapes;

import Hilligans.Client.Rendering.World.Managers.BlockTextureManager;
import Hilligans.Client.Rendering.World.Managers.VertexManagers.PlantManager;
import Hilligans.Data.Other.BlockState;
import Hilligans.Util.Vector5f;

public class XBlockShape extends BlockShape {

    @Override
    public Vector5f[] getVertices(int side, BlockState blockState, BlockTextureManager blockTextureManager) {
        return getVertices(side,0.5f,blockState,blockTextureManager);
    }

    @Override
    public Vector5f[] getVertices(int side, float size, BlockState blockState, BlockTextureManager blockTextureManager) {
        Vector5f[] vector5fs = PlantManager.getXBlockVertices(0.5f,0.5f,blockTextureManager,side,size);
        applyColoring(vector5fs,side);
        return vector5fs;
    }

    public Vector5f[] getVertices(int side, float size,float offsetX, float offsetZ, BlockState blockState, BlockTextureManager blockTextureManager) {
        Vector5f[] vector5fs = PlantManager.getXBlockVertices( offsetX + 0.5f, offsetZ + 0.5f,blockTextureManager,side,size);
        applyColoring(vector5fs,side);

        return vector5fs;
    }


    @Override
    public Integer[] getIndices(int side, int spot) {
        return super.getIndices(side, spot);
    }
}
