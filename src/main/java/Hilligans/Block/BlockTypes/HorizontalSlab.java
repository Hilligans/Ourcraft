package Hilligans.Block.BlockTypes;

import Hilligans.Block.Block;
import Hilligans.Client.Rendering.World.CubeManager;
import Hilligans.Data.Other.BoundingBox;
import Hilligans.Util.Vector5f;

public class HorizontalSlab extends Block {
    public HorizontalSlab(String name) {
        super(name);
        transparentTexture = true;
    }

    @Override
    public Vector5f[] getVertices(int side, float size) {
        return CubeManager.getSlabVertices(blockTextureManager,side,size,0);
    }

    protected BoundingBox getBoundingBox() {
        return new BoundingBox(0,0,0,1f,0.5f,1f);
    }

}
