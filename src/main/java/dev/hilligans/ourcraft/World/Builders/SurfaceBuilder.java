package dev.hilligans.ourcraft.World.Builders;

import dev.hilligans.ourcraft.Block.Block;
import dev.hilligans.ourcraft.Block.Blocks;
import dev.hilligans.ourcraft.Data.Other.BlockPos;
import dev.hilligans.ourcraft.Util.Settings;

public abstract class SurfaceBuilder extends WorldBuilder {
    public SurfaceBuilder(String featureName) {
        super(featureName);
    }

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
