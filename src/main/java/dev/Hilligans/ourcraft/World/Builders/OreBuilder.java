package dev.Hilligans.ourcraft.World.Builders;

import dev.Hilligans.ourcraft.Block.Block;
import dev.Hilligans.ourcraft.Data.Other.BlockPos;
import it.unimi.dsi.fastutil.ints.Int2BooleanOpenHashMap;

import java.util.ArrayList;
import java.util.Collection;

public class OreBuilder extends RandomBuilder {

    public Int2BooleanOpenHashMap allowedBlocks = new Int2BooleanOpenHashMap();
    public Block ore;

    public OreBuilder(String name, Block ore, Block... blocks) {
        super(name);
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
        if(allowedBlocks.get(world.getBlockState(pos).getBlock().id)) {
            world.setBlockState(pos,ore.getDefaultState());
        }
    }

    @Override
    public ArrayList<Block> getBlockList() {
        ArrayList<Block> arrayList = new ArrayList<>(1);
        arrayList.add(ore);
        return arrayList;
    }
}
