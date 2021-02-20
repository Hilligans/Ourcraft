package Hilligans.Data.Other.BlockShapes;

import Hilligans.Data.Other.BlockState;
import Hilligans.Client.Rendering.World.BlockTextureManager;
import Hilligans.Client.Rendering.World.CubeManager;
import Hilligans.Data.Other.BlockPos;
import Hilligans.Data.Other.BoundingBox;
import Hilligans.Data.Other.DataBlockState;
import Hilligans.Util.Vector5f;
import Hilligans.World.World;

public class HorizontalSlabBlockShape extends BlockShape {

    @Override
    public Vector5f[] getVertices(int side, float size, BlockState blockState, BlockTextureManager blockTextureManager) {
        Vector5f[] vector5fs;
        if(((DataBlockState)blockState).readData() == 0) {
            vector5fs = CubeManager.getHorizontalSlabVertices(blockTextureManager, side, size, 0);
        } else {
            vector5fs = CubeManager.getHorizontalSlabVertices(blockTextureManager, side, size, 0.5f);
        }
        applyColoring(vector5fs,side);
        return vector5fs;
    }


    public BoundingBox getBoundingBox(World world, BlockPos pos) {
        BlockState blockState = world.getBlockState(pos);
        if(((DataBlockState)blockState).readData() == 0) {
            return new BoundingBox(0, 0, 0, 1f, 0.5f, 1f);
        } else {
            return new BoundingBox(0, 0.5f, 0, 1f, 1.0f, 1f);
        }
    }

}
