package dev.Hilligans.ourcraft.World.NewWorldSystem;

import dev.Hilligans.ourcraft.Block.BlockState.IBlockState;

import java.util.concurrent.atomic.AtomicInteger;

public class PalettedSubChunk implements ISubChunk {

    public IChunk chunk;


    @Override
    public int getWidth() {
        return 16;
    }

    @Override
    public int getHeight() {
        return 16;
    }

    @Override
    public IBlockState getBlockState(long x, long y, long z) {
        return null;
    }

    @Override
    public IBlockState setBlockState(long x, long y, long z, IBlockState blockState) {
        return null;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    public static int minContainerSize = 4;


    public static class Lock {

        public AtomicInteger lockCount = new AtomicInteger();
        public boolean locked = false;

        public Lock() {
            //lockCount.get()
        }

    }
}
