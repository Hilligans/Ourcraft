package Hilligans.Block.BlockTypes;

import Hilligans.Block.Block;
import Hilligans.Data.Other.BlockState;
import Hilligans.Data.Other.BlockShapes.HorizontalSlabBlockShape;
import Hilligans.Data.Other.DataBlockState;
import Hilligans.Data.Other.RayResult;
import Hilligans.World.DataProviders.ShortBlockState;
import org.joml.Vector3f;

public class HorizontalSlab extends Block {
    public HorizontalSlab(String name) {
        super(name);
        transparentTexture = true;
        blockShape = new HorizontalSlabBlockShape();
    }

    @Override
    public BlockState getDefaultState() {
        return new DataBlockState(this, new ShortBlockState((short) 4));
    }

    @Override
    public BlockState getStateForPlacement(Vector3f playerPos, RayResult rayResult) {
        return new DataBlockState(this, new ShortBlockState((short) rayResult.side));
        /*
        if(Math.round(pos.y) != Math.floor(pos.y)) {
            return new DataBlockState(this,new ShortBlockState((short)0));
        } else {
            return new DataBlockState(this,new ShortBlockState((short)1));
        }

         */
        //return new DataBlockState(this,new ShortBlockState((short) (Math.random() * 5)));
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
