package dev.Hilligans.ourcraft.Block.BlockState;

public interface IBlockState {

    void register(IBlockStateType<?> type);

    void build(int id);

    Object getValue(IBlockStateType<?> type);

    IBlockState getNewState(IBlockStateType<?> stateType, Object obj);

    int getBlockStateID();
}
