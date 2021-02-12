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
        if(blockState.readData() == 0) {
            return CubeManager.getVerticalSlabVertices(blockTextureManager, side, size, 0, false);
        } else if(blockState.readData() == 1) {
            return CubeManager.getVerticalSlabVertices(blockTextureManager, side, size, 0.5f, false);
        } else if(blockState.readData() == 2) {
            return CubeManager.getVerticalSlabVertices(blockTextureManager, side, size, 0, true);
        } else {
            return CubeManager.getVerticalSlabVertices(blockTextureManager, side, size, 0.5f, true);
        }
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

    protected BoundingBox getBoundingBox(BlockState blockState) {
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
