package Hilligans.Item;

import Hilligans.Block.Block;

public class BlockItem extends Item {

    public Block block;

    public BlockItem(String name, Block block) {
        super(name);
        this.block = block;
    }
}
