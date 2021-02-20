package Hilligans.Block.BlockTypes;

import Hilligans.Block.Block;
import Hilligans.Data.Other.BlockState;
import Hilligans.Data.Other.BlockShapes.VerticalSlabBlockShape;
import Hilligans.Data.Other.DataBlockState;
import Hilligans.World.DataProviders.ShortBlockState;
import org.joml.Vector3f;

public class VerticalSlab extends Block {

    public VerticalSlab(String name) {
        super(name);
        transparentTexture = true;
        blockShape = new VerticalSlabBlockShape();
    }

    @Override
    public BlockState getDefaultState() {
        return new DataBlockState(this, new ShortBlockState((short) 0));
    }

    @Override
    public BlockState getStateForPlacement(Vector3f pos, Vector3f playerPos) {
        float x = Math.abs(pos.x) - Math.abs(playerPos.x);
        float z = Math.abs(pos.z) - Math.abs(playerPos.z);
        if(Math.abs(x) > Math.abs(z)) {
            if(Math.round(pos.x) != Math.floor(pos.x)) {
                return new DataBlockState(this,new ShortBlockState((short)0));
            } else {
                return new DataBlockState(this,new ShortBlockState((short)1));
            }
        } else {
            if(Math.round(pos.z) != Math.floor(pos.z)) {
                return new DataBlockState(this,new ShortBlockState((short)2));
            } else {
                return new DataBlockState(this,new ShortBlockState((short)3));
            }
        }
    }

    @Override
    public boolean hasBlockState() {
        return true;
    }

    @Override
    public BlockState getStateWithData(short data) {
        return new DataBlockState(this, new ShortBlockState(data));
    }

}
