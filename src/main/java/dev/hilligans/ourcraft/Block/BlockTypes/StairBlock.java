package dev.hilligans.ourcraft.Block.BlockTypes;

import dev.hilligans.ourcraft.Block.Block;
import dev.hilligans.ourcraft.Data.Other.BlockShapes.StairBlockShape;
import dev.hilligans.ourcraft.Data.Other.BlockStates.BlockState;
import dev.hilligans.ourcraft.Data.Other.BlockStates.DataBlockState;
import dev.hilligans.ourcraft.Data.Other.BlockProperties;
import dev.hilligans.ourcraft.World.DataProviders.ShortBlockState;

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
    public BlockState getDefaultState() {
        return new DataBlockState(this, new ShortBlockState((short)0));
    }

    @Override
    public BlockState getStateWithData(short data) {
        return new DataBlockState(this,new ShortBlockState(data));
    }
}
