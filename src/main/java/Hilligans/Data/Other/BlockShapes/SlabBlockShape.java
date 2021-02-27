package Hilligans.Data.Other.BlockShapes;

import Hilligans.Data.Other.BlockState;
import Hilligans.Client.Rendering.World.Managers.BlockTextureManager;
import Hilligans.Client.Rendering.World.Managers.VertexManagers.CubeManager;
import Hilligans.Data.Other.BlockPos;
import Hilligans.Data.Other.BoundingBox;
import Hilligans.Data.Other.DataBlockState;
import Hilligans.Util.Vector5f;
import Hilligans.World.World;

public class SlabBlockShape extends BlockShape {

    @Override
    public Vector5f[] getVertices(int side, float size, BlockState blockState, BlockTextureManager blockTextureManager) {
        Vector5f[] vector5fs;
        switch (((DataBlockState)blockState).readData()) {
            case 3:
                vector5fs = CubeManager.getVerticalSlabVertices(blockTextureManager, side, size, 0, false);
                break;
            case 2:
                vector5fs = CubeManager.getVerticalSlabVertices(blockTextureManager, side, size, 0.5f, false);
                break;
            case 1:
                vector5fs = CubeManager.getVerticalSlabVertices(blockTextureManager, side, size, 0, true);
                break;
            case 0:
                vector5fs = CubeManager.getVerticalSlabVertices(blockTextureManager, side, size, 0.5f, true);
                break;
            case 5:
                vector5fs = CubeManager.getHorizontalSlabVertices(blockTextureManager, side, size, 0);
                break;
            default:
                vector5fs = CubeManager.getHorizontalSlabVertices(blockTextureManager, side, size, 0.5f);
                break;
        }
        applyColoring(vector5fs,side);
        return vector5fs;
    }

    @Override
    public BoundingBox getBoundingBox(World world, BlockPos pos) {
        switch (((DataBlockState)world.getBlockState(pos)).readData()) {
            case 5:
                return new BoundingBox(0, 0, 0, 1f, 0.5f, 1f);
            case 4:
                return new BoundingBox(0, 0.5f, 0, 1f, 1.0f, 1f);
            case 3:
                return new BoundingBox(0,0,0,0.5f,1,1f);
            case 2:
                return new BoundingBox(0.5f,0,0,1,1,1f);
            case 1:
                return new BoundingBox(0,0,0,1f,1,0.5f);
            default:
                return new BoundingBox(0,0,0.5f,1f,1,1);
        }
    }

}
