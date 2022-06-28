package dev.Hilligans.ourcraft.World;

import dev.Hilligans.ourcraft.Data.Other.BlockStates.BlockState;

public interface IChunk {

    int getWidth();
    int getHeight();

    long getX();
    long getY();
    long getZ();

    BlockState getBlockState(long x, long y, long z);
    void setBlockState(long x, long y, long z, BlockState blockState);
}
