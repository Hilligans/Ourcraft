package dev.hilligans.ourcraft.Block.BlockTypes;

import dev.hilligans.ourcraft.Block.Block;
import dev.hilligans.ourcraft.Data.Other.BlockProperties;
import dev.hilligans.ourcraft.Data.Other.BlockStates.BlockState;
import dev.hilligans.ourcraft.Data.Other.BlockStates.DataBlockState;
import dev.hilligans.ourcraft.World.DataProviders.ShortBlockState;

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
    public BlockState getDefaultState() {
        return new DataBlockState(this, new ShortBlockState((short) new Random().nextInt()));
    }

    @Override
    public BlockState getStateWithData(short data) {
        return new DataBlockState(this, new ShortBlockState(data));
    }
}
