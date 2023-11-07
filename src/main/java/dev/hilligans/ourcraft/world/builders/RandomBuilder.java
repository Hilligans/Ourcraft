package dev.hilligans.ourcraft.world.builders;

import dev.hilligans.ourcraft.data.other.BlockPos;
import dev.hilligans.ourcraft.util.Settings;

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
