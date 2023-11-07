package dev.hilligans.ourcraft.util.game.resource;

import dev.hilligans.ourcraft.block.Block;

public class BlockGameResource extends GameResource {

    public Block block;

    public BlockGameResource(Block block) {
        this.block = block;
    }

    @Override
    public String toString() {
        return "BlockGameResource{" +
                "block=" + block.getUniqueName() +
                '}';
    }
}
