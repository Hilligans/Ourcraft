package Hilligans.World.Builders;

import Hilligans.Blocks.Block;
import Hilligans.World.BlockPos;
import Hilligans.World.World;
import it.unimi.dsi.fastutil.ints.Int2BooleanOpenHashMap;

public class OreBuilder extends RandomBuilder {

    Int2BooleanOpenHashMap allowedBlocks = new Int2BooleanOpenHashMap();
    Block ore;

    int size = 3;

    public OreBuilder(Block ore, Block... blocks) {
        this.ore = ore;
        for(Block block : blocks) {
            allowedBlocks.put(block.id,true);
        }
    }

    @Override
    public void build(BlockPos pos) {
        replaceIfValid(pos);
        for(int x = 0; x < 6; x++) {
            replaceIfValid(pos.copy().add(Block.getBlockPos(x)));
        }
    }

    public void replaceIfValid(BlockPos pos) {
        if(allowedBlocks.get(world.getBlockState(pos).block.id)) {
            world.setBlockState(pos,ore.getDefaultState());
        }
    }
}
