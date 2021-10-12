package dev.Hilligans.ourcraft.Block.BlockTypes;

import dev.Hilligans.ourcraft.Block.Block;
import dev.Hilligans.ourcraft.Data.Other.BlockProperties;
import dev.Hilligans.ourcraft.Data.Other.BlockStates.BlockState;
import dev.Hilligans.ourcraft.Data.Other.BlockStates.DataBlockState;
import dev.Hilligans.ourcraft.World.DataProviders.ShortBlockState;

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
