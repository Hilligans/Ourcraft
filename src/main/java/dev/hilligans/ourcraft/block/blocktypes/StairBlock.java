package dev.hilligans.ourcraft.block.blocktypes;

import dev.hilligans.engine.GameInstance;
import dev.hilligans.ourcraft.block.Block;
import dev.hilligans.ourcraft.data.other.BlockProperties;
import dev.hilligans.ourcraft.data.other.blockshapes.StairBlockShape;
import dev.hilligans.ourcraft.data.other.blockstates.BlockState;
import dev.hilligans.ourcraft.data.other.blockstates.DataBlockState;
import dev.hilligans.ourcraft.world.data.providers.ShortBlockState;

public class StairBlock extends Block {
    public StairBlock(String name, BlockProperties blockProperties) {
        super(name, blockProperties);
        blockProperties.transparent();
    }

    @Override
    public boolean hasBlockState() {
        return true;
    }

    /*
    @Override
    public BlockState getDefaultState() {
        return new DataBlockState(this, new ShortBlockState((short)0));
    }
     */

    @Override
    public void load(GameInstance gameInstance) {
        blockProperties.blockShape = new StairBlockShape(gameInstance);
    }

    @Override
    public BlockState getStateWithData(short data) {
        return new DataBlockState(this,new ShortBlockState(data));
    }
}
