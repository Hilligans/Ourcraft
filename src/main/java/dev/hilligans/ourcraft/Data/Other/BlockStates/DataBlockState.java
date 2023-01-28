package dev.hilligans.ourcraft.Data.Other.BlockStates;

import dev.hilligans.ourcraft.Block.Block;
import dev.hilligans.ourcraft.World.BlockStateDataProvider;

import java.util.Objects;

public class DataBlockState extends BlockState {

    public BlockStateDataProvider blockData;

    public DataBlockState(Block block) {
        super(block);
    }

    public DataBlockState(Block block, BlockStateDataProvider blockStateDataProvider) {
        super(block);
        blockData = blockStateDataProvider;
    }

    public DataBlockState(Short blockId, BlockStateDataProvider blockStateDataProvider) {
        super(blockId);
        blockData = blockStateDataProvider;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        DataBlockState that = (DataBlockState) o;
        return blockData.write() == ((DataBlockState) o).blockData.write();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), blockData);
    }

    public short readData() {
        if(blockData == null) {
            return 0;
        }
        return blockData.write();
    }

    public BlockState write(short in) {
        if(blockData != null) {
            blockData.read(in);
        }
        return this;
    }

    public BlockState duplicate() {
        return new DataBlockState(blockId,blockData.duplicate());
    }
}
