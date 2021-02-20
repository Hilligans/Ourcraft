package Hilligans.Data.Other;

import Hilligans.Block.Block;
import Hilligans.Block.Blocks;
import Hilligans.World.BlockStateDataProvider;

public class BlockState {

    //public Block block;

    public short blockId;

    public BlockState(Block block) {
        this.blockId = block.id;
    }

    public Block getBlock() {
        return Blocks.getBlockWithID(blockId);
    }




}
