package dev.Hilligans.Ourcraft.World.Builders;

import dev.Hilligans.Ourcraft.Block.Block;
import dev.Hilligans.Ourcraft.Block.Blocks;
import dev.Hilligans.Ourcraft.Data.Other.BlockPos;
import dev.Hilligans.Ourcraft.Util.Settings;

public abstract class SurfaceBuilder extends WorldBuilder {
    @Override
    public void build(int x, int z) {
        int y = Settings.chunkHeight * 16;
        while(y > 0 && world.getBlockState(x,y,z).getBlock() == Blocks.AIR) {
            y--;
        }
        y++;
        build(new BlockPos(x,y,z));
    }

    public boolean isPlacedOn(BlockPos pos, Block block) {
        return world.getBlockState(pos.copy().add(0,-1,0)).getBlock() == block;
    }

    public abstract void build(BlockPos startPos);
}
