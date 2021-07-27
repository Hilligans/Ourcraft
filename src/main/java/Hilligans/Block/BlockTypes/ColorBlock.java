package Hilligans.Block.BlockTypes;

import Hilligans.Block.Block;
import Hilligans.Data.Other.BlockProperties;
import Hilligans.Data.Other.BlockStates.BlockState;
import Hilligans.Data.Other.BlockStates.DataBlockState;
import Hilligans.World.DataProviders.ShortBlockState;

import java.util.Random;

public class ColorBlock extends Block {
    public ColorBlock(String name, BlockProperties blockProperties) {
        super(name, blockProperties);
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
        return new DataBlockState(this, new ShortBlockState((short) new Random().nextInt()));
    }

    @Override
    public BlockState getStateWithData(short data) {
        return new DataBlockState(this, new ShortBlockState(data));
    }
}
