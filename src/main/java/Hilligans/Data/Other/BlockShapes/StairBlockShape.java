package Hilligans.Data.Other.BlockShapes;

import Hilligans.Client.Rendering.World.Managers.BlockTextureManager;
import Hilligans.Client.Rendering.World.Managers.VertexManagers.CubeManager;
import Hilligans.Data.Other.*;
import Hilligans.Util.Vector5f;
import Hilligans.World.World;

public class StairBlockShape extends BlockShape {

    @Override
    public Vector5f[] getVertices(int side, float size, BlockState blockState, BlockTextureManager blockTextureManager) {
        Vector5f[] vector5fs = CubeManager.getStairVertices(blockTextureManager,side,1.0f,0,0);
        applyColoring(vector5fs,side);
        return vector5fs;
    }

    @Override
    public BoundingBox getBoundingBox(World world, BlockPos pos) {
        return new JoinedBoundingBox(0,0,0,1,0.5f,1).addBox(0,0.5f,0.5f,1,1,1);
    }

    public Integer[] getIndices(int side, int spot) {
        if(side == 0 || side == 1 || side == 2) {
            return getIndices(side, spot,false);
        }
        return new Integer[]{};
    }

    public static Integer[] getIndices(int side, int spot, boolean a) {
        switch (side) {
            case 0:
            case 5:
            case 3:
                return new Integer[] {spot,spot + 1,spot + 2,spot,spot + 2,spot + 3};
            case 2:
                return new Integer[]{spot,spot + 2, spot + 1, spot, spot + 3, spot + 2, spot + 4,spot + 6, spot + 5, spot + 4, spot + 7, spot + 6};
            case 6:
                return new Integer[]{spot,spot + 1,spot + 2,spot,spot + 2,spot + 3};

            default:
                return new Integer[]{spot,spot + 2, spot + 1, spot, spot + 3, spot + 2};
        }
    }



}
