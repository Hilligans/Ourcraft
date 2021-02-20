package Hilligans.Data.Other.BlockShapes;

import Hilligans.Data.Other.BlockState;
import Hilligans.Client.Rendering.World.BlockTextureManager;
import Hilligans.Client.Rendering.World.CubeManager;
import Hilligans.Data.Other.BlockPos;
import Hilligans.Data.Other.BoundingBox;
import Hilligans.Data.Other.DataBlockState;
import Hilligans.Util.Vector5f;
import Hilligans.World.World;

public class VerticalSlabBlockShape extends BlockShape {


    @Override
    public Vector5f[] getVertices(int side, float size, BlockState blockState, BlockTextureManager blockTextureManager) {
        Vector5f[] vector5fs;
        if(((DataBlockState)blockState).readData() == 0) {
            vector5fs = CubeManager.getVerticalSlabVertices(blockTextureManager, side, size, 0, false);
        } else if(((DataBlockState)blockState).readData() == 1) {
            vector5fs = CubeManager.getVerticalSlabVertices(blockTextureManager, side, size, 0.5f, false);
        } else if(((DataBlockState)blockState).readData() == 2) {
            vector5fs = CubeManager.getVerticalSlabVertices(blockTextureManager, side, size, 0, true);
        } else {
            vector5fs = CubeManager.getVerticalSlabVertices(blockTextureManager, side, size, 0.5f, true);
        }
        applyColoring(vector5fs,side);
        return vector5fs;
    }

    @Override
    public BoundingBox getBoundingBox(World world, BlockPos pos) {
        switch (((DataBlockState)world.getBlockState(pos)).readData()) {
            case 0:
                return new BoundingBox(0,0,0,0.5f,1,1f);
            case 1:
                return new BoundingBox(0.5f,0,0,1,1,1f);
            case 2:
                return new BoundingBox(0,0,0,1f,1,0.5f);
            default:
                return new BoundingBox(0,0,0.5f,1f,1,1);
        }
    }
}
