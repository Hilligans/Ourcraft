package dev.hilligans.ourcraft.block.blocktypes;

import dev.hilligans.ourcraft.block.Block;
import dev.hilligans.ourcraft.data.other.BlockProperties;
import dev.hilligans.ourcraft.data.other.blockstates.BlockState;
import dev.hilligans.ourcraft.data.other.blockstates.DataBlockState;
import dev.hilligans.ourcraft.world.data.providers.ShortBlockState;

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
