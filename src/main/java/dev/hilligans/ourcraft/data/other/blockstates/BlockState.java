package dev.hilligans.ourcraft.data.other.blockstates;

import dev.hilligans.engine.GameInstance;
import dev.hilligans.ourcraft.block.Block;
import dev.hilligans.ourcraft.block.Blocks;

import java.util.Objects;

public class BlockState {

    public short blockId;

    public BlockState(Block block) {
        this.blockId = block.id;
    }

    public BlockState(short blockId) {
        this.blockId = blockId;
    }

    public Block getBlock(GameInstance gameInstance) {
        return gameInstance.BLOCKS.get(blockId);
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

    public int get() {
        return blockId << 16 | readData();
    }

    @Override
    public int hashCode() {
        return Objects.hash(get());
    }

    public BlockState duplicate() {
        return new BlockState(blockId);
    }
}
