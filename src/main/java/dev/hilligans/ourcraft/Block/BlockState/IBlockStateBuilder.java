package dev.hilligans.ourcraft.Block.BlockState;

import dev.hilligans.ourcraft.Block.Block;

import java.util.Collection;

public interface IBlockStateBuilder {

    void register(IBlockStateType<?> stateType);

    void setBlock(Block block);

    Collection<IBlockStateType<?>> getStateTypes();

    IBlockState build(int id);

    IBlockStateType<?> getBlockStateType(int index);

    int getSize();
}
