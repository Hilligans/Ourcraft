package Hilligans.World.Builders;

import Hilligans.Block.Block;
import Hilligans.Block.Blocks;
import Hilligans.Util.Settings;
import Hilligans.Data.Other.BlockPos;

public abstract class SurfaceBuilder extends WorldBuilder {
    @Override
    public void build(int x, int z) {
        int y = Settings.chunkHeight * 16;
        while(y > 0 && world.getBlockState(x,y,z).block == Blocks.AIR) {
            y--;
        }
        y++;
        build(new BlockPos(x,y,z));
    }

    public boolean isPlacedOn(BlockPos pos, Block block) {
        return world.getBlockState(pos.copy().add(0,-1,0)).block == block;
    }

    public abstract void build(BlockPos startPos);
}
