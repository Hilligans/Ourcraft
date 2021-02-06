package Hilligans.World.Builders;

import Hilligans.Util.Settings;
import Hilligans.Data.Other.BlockPos;

public abstract class RandomBuilder extends WorldBuilder {

    @Override
    public void build(int x, int z) {
        int y = (int)(Math.random() * Settings.chunkHeight * 16);
        build(new BlockPos(x,y,z));
    }

    public abstract void build(BlockPos pos);
}
