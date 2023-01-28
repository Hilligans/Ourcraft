package dev.hilligans.ourcraft.Util.GameResource;

import dev.hilligans.ourcraft.Block.Block;

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
