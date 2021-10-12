package dev.Hilligans.Ourcraft.World.Builders;

import dev.Hilligans.Ourcraft.Data.Other.BlockPos;
import dev.Hilligans.Ourcraft.Util.Settings;

public abstract class RandomBuilder extends WorldBuilder {

    @Override
    public void build(int x, int z) {
        int y = (int)(Math.random() * Settings.chunkHeight * 16);
        build(new BlockPos(x,y,z));
    }

    public abstract void build(BlockPos pos);
}
