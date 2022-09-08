package dev.Hilligans.ourcraft.Block.BlockState;

import dev.Hilligans.ourcraft.Block.Block;

public interface IBlockState {

    Block getBlock();
    void register(IBlockStateType<?> type);

    void build(int id);

    Object getValue(IBlockStateType<?> type);

    IBlockState getNewState(IBlockStateType<?> stateType, Object obj);

    int getBlockStateID();
}
