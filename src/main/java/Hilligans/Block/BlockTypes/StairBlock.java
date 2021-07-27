package Hilligans.Block.BlockTypes;

import Hilligans.Block.Block;
import Hilligans.Data.Other.*;
import Hilligans.Data.Other.BlockShapes.StairBlockShape;
import Hilligans.Data.Other.BlockStates.BlockState;
import Hilligans.Data.Other.BlockStates.DataBlockState;
import Hilligans.World.DataProviders.ShortBlockState;

public class StairBlock extends Block {
    public StairBlock(String name, BlockProperties blockProperties) {
        super(name, blockProperties);
        blockProperties.blockShape = new StairBlockShape();
        blockProperties.transparent();
    }


    @Override
    public boolean hasBlockState() {
        return true;
    }

    @Override
    public int blockStateByteCount() {
        return 2;
    }

    @Override
    public BlockState getDefaultState() {
        return new DataBlockState(this, new ShortBlockState((short)0));
    }

    @Override
    public BlockState getStateWithData(short data) {
        return new DataBlockState(this,new ShortBlockState(data));
    }
}
