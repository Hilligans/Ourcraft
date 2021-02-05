package Hilligans.World.Builders;

import Hilligans.Blocks.Blocks;
import Hilligans.Util.Settings;
import Hilligans.World.BlockPos;
import Hilligans.World.World;

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

    public abstract void build(BlockPos startPos);
}
