package dev.hilligans.ourcraft.world.newworldsystem;

import dev.hilligans.ourcraft.block.blockstate.IBlockState;

import java.util.concurrent.atomic.AtomicInteger;

public class PalettedSubChunk implements IAtomicSubChunk {

    public AtomicInteger handleCount = new AtomicInteger();
    public volatile boolean blocking;

    public int paletteWidth = 4;
    public int[] blocks;
    public short[] palette;

    public PalettedSubChunk() {
        palette = new short[1 << paletteWidth];
    }

    public PalettedSubChunk(SingleBlockFinalSubChunk from) {
        palette = new short[1 << paletteWidth];
        palette[0] = (short) from.blockState.getBlockStateID();
    }

    @Override
    public int getWidth() {
        return 16;
    }

    @Override
    public int getHeight() {
        return 16;
    }

    @Override
    public IBlockState getBlockState(int x, int y, int z) {
        return null;
    }

    @Override
    public IBlockState setBlockState(int x, int y, int z, IBlockState blockState) {
        return null;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public ISubChunk canInsertOrGetNext(IBlockState blockState) {
        //TODO implement
        return null;
    }

    @Override
    public IBlockState setBlockStateAtomic(int x, int y, int z, IBlockState blockState) {
        return null;
    }

    @Override
    public boolean swapBlockStateAtomic(int x, int y, int z, IBlockState expected, IBlockState to) {
        return false;
    }

    public boolean grow() {
        if(paletteWidth >= 8) {
            return false;
        }
        blocking = true;
        //spin until everything is done setting
        while (handleCount.get() != 0) {}
        paletteWidth++;
        if(paletteWidth == 7) {
            paletteWidth = 8;
        }
        int count = Math.ceilDiv(16 * 16 * 16, paletteWidth);
        int[] newVals = new int[count];


        int and = 1;
        for(int x = 0; x < paletteWidth; x++) {
            and = and << 1 | 1;
        }

        int c = 0;
        int i;
        int b;
        while (c != 16 * 16 * 16) {
            //newVals


            c++;
        }

        blocking = false;
        return true;
    }

    public static int getIndex(int x, int y, int z, int width) {
        return 0;
    }
}
