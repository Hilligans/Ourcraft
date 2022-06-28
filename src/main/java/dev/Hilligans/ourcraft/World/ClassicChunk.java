package dev.Hilligans.ourcraft.World;

import dev.Hilligans.ourcraft.Block.Blocks;
import dev.Hilligans.ourcraft.Data.Other.BlockStates.BlockState;

public class ClassicChunk implements IChunk {

    public int x;
    public int z;
    public int height;

    public SubChunk[] chunks;
    public World world;

    public ClassicChunk(World world, int height, int x, int z) {
        this.world = world;
        this.height = height;
        this.x = x;
        this.z = z;
        chunks = new SubChunk[height];
    }

    @Override
    public int getWidth() {
        return 16;
    }

    @Override
    public int getHeight() {
        return height * 16;
    }

    @Override
    public long getX() {
        return x;
    }

    @Override
    public long getY() {
        return 0;
    }

    @Override
    public long getZ() {
        return z;
    }

    @Override
    public BlockState getBlockState(long x, long y, long z) {
        SubChunk subChunk = chunks[(int) (y >> 4)];
        if(subChunk == null) {
            return Blocks.AIR.getDefaultState();
        }
        return subChunk.getBlock((int) (x % 15), (int) (y % 15), (int) (z % 15));
    }

    @Override
    public void setBlockState(long x, long y, long z, BlockState blockState) {
        SubChunk subChunk = chunks[(int) (y >> 4)];
        if(subChunk == null) {
            subChunk = chunks[(int) (y >> 4)] = new SubChunk(world,x * 16L,(y >> 4) * 16,z * 16L);
        }
        subChunk.setBlockState((int) (x % 15), (int) (y % 15), (int) (z % 15),blockState);
    }
}
