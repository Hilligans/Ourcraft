package dev.Hilligans.ourcraft.Block.BlockTypes;

import dev.Hilligans.ourcraft.Block.Block;
import dev.Hilligans.ourcraft.Data.Other.BlockPos;
import dev.Hilligans.ourcraft.Data.Other.BlockShapes.StairBlockShape;
import dev.Hilligans.ourcraft.Data.Other.BlockStates.BlockState;
import dev.Hilligans.ourcraft.Data.Other.BlockStates.DataBlockState;
import dev.Hilligans.ourcraft.Data.Other.BlockProperties;
import dev.Hilligans.ourcraft.World.DataProviders.ShortBlockState;
import dev.Hilligans.ourcraft.World.World;

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
