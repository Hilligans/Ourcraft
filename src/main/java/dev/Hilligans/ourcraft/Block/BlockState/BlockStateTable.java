package dev.Hilligans.ourcraft.Block.BlockState;

import dev.Hilligans.ourcraft.Util.Registry.Registry;

import java.util.ArrayList;

public class BlockStateTable implements IBlockStateTable {

    public ArrayList<IBlockState> states;
    public int offsetIndex;

    public BlockStateTable(ArrayList<IBlockState> states, int offsetIndex) {
        this.states = states;
        this.offsetIndex = offsetIndex;
    }

    @Override
    public IBlockState getBlockState(int id) {
        return states.get(offsetIndex + id);
    }
}
