package dev.Hilligans.Ourcraft.Block.BlockTypes;

import dev.Hilligans.Ourcraft.Block.Block;
import dev.Hilligans.Ourcraft.Data.Other.BlockShapes.StairBlockShape;
import dev.Hilligans.Ourcraft.Data.Other.BlockStates.BlockState;
import dev.Hilligans.Ourcraft.Data.Other.BlockStates.DataBlockState;
import dev.Hilligans.Ourcraft.Data.Other.BlockProperties;
import dev.Hilligans.Ourcraft.World.DataProviders.ShortBlockState;

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
