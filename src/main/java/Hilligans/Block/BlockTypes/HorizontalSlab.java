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
        if(blockState.readData() == 0) {
            return CubeManager.getHorizontalSlabVertices(blockTextureManager, side, size, 0);
        } else {
            return CubeManager.getHorizontalSlabVertices(blockTextureManager, side, size, 0.5f);
        }
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

    protected BoundingBox getBoundingBox(BlockState blockState) {
        if(blockState.readData() == 0) {
            return new BoundingBox(0, 0, 0, 1f, 0.5f, 1f);
        } else {
            return new BoundingBox(0, 0.5f, 0, 1f, 1.0f, 1f);
        }
    }

}
