package Hilligans.Data.Other;

import Hilligans.Block.Block;
import Hilligans.Block.Blocks;
import Hilligans.World.BlockStateDataProvider;

import java.util.Objects;

public class BlockState {

    //public Block block;

    public short blockId;

    public BlockState(Block block) {
        this.blockId = block.id;
    }

    public BlockState(short blockId) {
        this.blockId = blockId;
    }

    public Block getBlock() {
        return Blocks.getBlockWithID(blockId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BlockState that = (BlockState) o;
        return blockId == that.blockId;
    }

    public short readData() {
        return -1;
    }

    @Override
    public int hashCode() {
        return Objects.hash(blockId);
    }

    public BlockState duplicate() {
        return new BlockState(blockId);
    }
}
