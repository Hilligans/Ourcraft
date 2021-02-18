package Hilligans.Block.BlockTypes;

import Hilligans.Block.Block;
import Hilligans.Block.BlockState;
import Hilligans.Client.Rendering.World.CubeManager;
import Hilligans.Data.Other.BoundingBox;
import Hilligans.Util.Vector5f;
import Hilligans.World.DataProviders.ShortBlockState;
import org.joml.Vector3f;

public class HorizontalSlab extends Block {
    public HorizontalSlab(String name) {
        super(name);
        transparentTexture = true;
    }

    @Override
    public Vector5f[] getVertices(int side, float size, BlockState blockState) {
        Vector5f[] vector5fs;
        if(blockState.readData() == 0) {
            vector5fs = CubeManager.getHorizontalSlabVertices(blockTextureManager, side, size, 0);
        } else {
            vector5fs = CubeManager.getHorizontalSlabVertices(blockTextureManager, side, size, 0.5f);
        }

        for(Vector5f vector5f : vector5fs) {
            if(side == 2 || side == 3) {
                vector5f.setColored(0.95f,0.95f,0.95f,1.0f);
            } else if(side == 0 || side == 1) {
                vector5f.setColored(0.9f,0.9f,0.9f,1.0f);
            } else if(side == 4) {
                vector5f.setColored(0.85f,0.85f,0.85f,1.0f);
            } else {
                vector5f.setColored();
            }
        }
        return vector5fs;
    }

    @Override
    public BlockState getDefaultState() {
        return new BlockState(this, new ShortBlockState((short) 0));
    }

    @Override
    public BlockState getStateForPlacement(Vector3f pos, Vector3f playerPos) {
        if(Math.round(pos.y) != Math.floor(pos.y)) {
            return new BlockState(this,new ShortBlockState((short)0));
        } else {
            return new BlockState(this,new ShortBlockState((short)1));
        }
    }

    @Override
    public BlockState getStateWithData(short data) {
        return new BlockState(this, new ShortBlockState(data));
    }

    public BoundingBox getBoundingBox(BlockState blockState) {
        if(blockState.readData() == 0) {
            return new BoundingBox(0, 0, 0, 1f, 0.5f, 1f);
        } else {
            return new BoundingBox(0, 0.5f, 0, 1f, 1.0f, 1f);
        }
    }

}
