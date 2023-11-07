package dev.hilligans.ourcraft.block.blockstate;

import dev.hilligans.ourcraft.block.Block;

import java.util.ArrayList;
import java.util.Collection;

public class BlockStateBuilder implements IBlockStateBuilder {

    public Block block;
    public int size = 1;
    public ArrayList<IBlockStateType<?>> blockStateTypes = new ArrayList<>();

    @Override
    public void register(IBlockStateType<?> stateType) {
        blockStateTypes.add(stateType);
        size *= stateType.getCount();
    }

    @Override
    public void setBlock(Block block) {
        this.block = block;
    }

    @Override
    public Collection<IBlockStateType<?>> getStateTypes() {
        return blockStateTypes;
    }

    @Override
    public IBlockState build(int id) {
        NewBlockState blockState = new NewBlockState(block);
        blockState.setStateBuilder(this);
        blockState.build(id);
        return blockState;
    }

    @Override
    public IBlockStateType<?> getBlockStateType(int index) {
        return blockStateTypes.get(index);
    }

    @Override
    public int getSize() {
        return size;
    }
}
