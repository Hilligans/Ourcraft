package Hilligans.Data.Other.BlockShapes;

import Hilligans.Client.Rendering.NewRenderer.BlockModel;
import Hilligans.Client.Rendering.World.Managers.BlockTextureManager;
import Hilligans.Client.Rendering.World.Managers.VertexManagers.PlantManager;
import Hilligans.Data.Other.BlockState;
import Hilligans.Util.Vector5f;

public class XBlockShape extends BlockShape {

    public XBlockShape() {
        data = BlockModel.create("/Models/Blocks/xBlock.txt");
    }

    @Override
    public Vector5f[] getVertices(int side, BlockState blockState, BlockTextureManager blockTextureManager) {
        return getVertices(side,0.5f,blockState,blockTextureManager);
    }

    @Override
    public Vector5f[] getVertices(int side, float size, BlockState blockState, BlockTextureManager blockTextureManager) {
        if(side != 6) {
            Vector5f[] vector5fs = PlantManager.getXBlockVertices(0.5f, 0.5f, blockTextureManager, side, size);
            applyColoring(vector5fs, side);
            return vector5fs;
        } else {
            return new Vector5f[]{};
        }
    }

    public Vector5f[] getVertices(int side, float size,float offsetX, float offsetZ, BlockState blockState, BlockTextureManager blockTextureManager) {
        if(side != 6) {
            Vector5f[] vector5fs = PlantManager.getXBlockVertices(offsetX + 0.5f, offsetZ + 0.5f, blockTextureManager, side, size);
            applyColoring(vector5fs, side);
            return vector5fs;
        } else {
            return new Vector5f[]{};
        }
    }


    @Override
    public Integer[] getIndices(int side, int spot) {
        return super.getIndices(side, spot);
    }
}
