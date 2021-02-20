package Hilligans.Block.BlockTypes;

import Hilligans.Block.Block;

public class StairBlock extends Block {
    public StairBlock(String name) {
        super(name);
    }


    @Override
    public boolean hasBlockState() {
        return true;
    }
}
