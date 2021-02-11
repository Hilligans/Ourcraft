package Hilligans.Block.BlockTypes;

import Hilligans.Block.Block;
import Hilligans.Block.BlockState;
import Hilligans.Client.Rendering.World.CubeManager;
import Hilligans.Data.Other.BoundingBox;
import Hilligans.Util.Vector5f;

public class VerticalSlab extends Block {

    public VerticalSlab(String name) {
        super(name);
        transparentTexture = true;
    }

    @Override
    public Vector5f[] getVertices(int side, float size, BlockState blockState) {
        return CubeManager.getVerticalSlabVertices(blockTextureManager,side,size,0,false);
    }

    protected BoundingBox getBoundingBox(BlockState blockState) {
        return new BoundingBox(0,0,0,0.5f,1,1f);
    }

}
