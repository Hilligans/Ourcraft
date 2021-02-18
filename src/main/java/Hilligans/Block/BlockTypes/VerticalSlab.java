package Hilligans.Block.BlockTypes;

import Hilligans.Block.Block;
import Hilligans.Block.BlockState;
import Hilligans.Client.Rendering.World.CubeManager;
import Hilligans.Data.Other.BoundingBox;
import Hilligans.Util.Vector5f;
import Hilligans.World.DataProviders.ShortBlockState;
import org.joml.Vector3f;

public class VerticalSlab extends Block {

    public VerticalSlab(String name) {
        super(name);
        transparentTexture = true;
    }

    @Override
    public Vector5f[] getVertices(int side, float size, BlockState blockState) {
        Vector5f[] vector5fs;
        if(blockState.readData() == 0) {
            vector5fs = CubeManager.getVerticalSlabVertices(blockTextureManager, side, size, 0, false);
        } else if(blockState.readData() == 1) {
            vector5fs = CubeManager.getVerticalSlabVertices(blockTextureManager, side, size, 0.5f, false);
        } else if(blockState.readData() == 2) {
            vector5fs = CubeManager.getVerticalSlabVertices(blockTextureManager, side, size, 0, true);
        } else {
            vector5fs = CubeManager.getVerticalSlabVertices(blockTextureManager, side, size, 0.5f, true);
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
        float x = Math.abs(pos.x) - Math.abs(playerPos.x);
        float z = Math.abs(pos.z) - Math.abs(playerPos.z);
        if(Math.abs(x) > Math.abs(z)) {
            if(Math.round(pos.x) != Math.floor(pos.x)) {
                return new BlockState(this,new ShortBlockState((short)0));
            } else {
                return new BlockState(this,new ShortBlockState((short)1));
            }
        } else {
            if(Math.round(pos.z) != Math.floor(pos.z)) {
                return new BlockState(this,new ShortBlockState((short)2));
            } else {
                return new BlockState(this,new ShortBlockState((short)3));
            }
        }
    }

    @Override
    public BlockState getStateWithData(short data) {
        return new BlockState(this, new ShortBlockState(data));
    }

    public BoundingBox getBoundingBox(BlockState blockState) {
        switch (blockState.readData()) {
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
