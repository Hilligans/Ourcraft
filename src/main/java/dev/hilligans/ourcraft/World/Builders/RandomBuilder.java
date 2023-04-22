package dev.hilligans.ourcraft.World.Builders;

import dev.hilligans.ourcraft.Data.Other.BlockPos;
import dev.hilligans.ourcraft.Util.Settings;

public abstract class RandomBuilder extends WorldBuilder {

    public RandomBuilder(String featureName) {
        super(featureName);
    }

    @Override
    public void build(int x, int z) {
        int y = (int)(Math.random() * Settings.chunkHeight * 16);
        build(new BlockPos(x,y,z));
    }

    public abstract void build(BlockPos pos);
}