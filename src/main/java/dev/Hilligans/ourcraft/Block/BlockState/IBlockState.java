package dev.Hilligans.ourcraft.Block.BlockState;

import dev.Hilligans.ourcraft.Block.Block;

public interface IBlockState {

    Block getBlock();

    void setStateBuilder(IBlockStateBuilder builder);

    IBlockStateBuilder getBuilder();

    Object getValue(IBlockStateType<?> type);

    IBlockState getNewState(IBlockStateType<?> stateType, Object obj);

    int getBlockStateID();

    int getBlockID();

    IBlockState setBlockStateID(int val);
}
