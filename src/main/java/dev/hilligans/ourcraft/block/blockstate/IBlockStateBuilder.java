package dev.hilligans.ourcraft.block.blockstate;

import dev.hilligans.ourcraft.block.Block;

import java.util.Collection;

public interface IBlockStateBuilder {

    void register(IBlockStateType<?> stateType);

    void setBlock(Block block);

    Collection<IBlockStateType<?>> getStateTypes();

    IBlockState build(int id);

    IBlockStateType<?> getBlockStateType(int index);

    int getSize();
}
