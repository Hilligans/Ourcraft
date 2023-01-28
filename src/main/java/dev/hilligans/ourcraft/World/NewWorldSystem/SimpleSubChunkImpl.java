package dev.hilligans.ourcraft.World.NewWorldSystem;

import dev.hilligans.ourcraft.Block.BlockState.IBlockState;
import dev.hilligans.ourcraft.Block.Blocks;

public class SimpleSubChunkImpl implements ISubChunk {

    public IBlockState[] blockStates;

    public int width;
    public int height;

    public SimpleSubChunkImpl(int width, int height) {
        blockStates = new IBlockState[width*height*width];
        this.width = width;
        this.height = height;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public IBlockState getBlockState(long x, long y, long z) {
        try {
            IBlockState b =  blockStates[(int) ((x * width + y) * height + z)];
            return b == null ? Blocks.AIR.getDefaultState1() : b;
        } catch (Exception e) {
           // e.printStackTrace();
        }
        return Blocks.AIR.getDefaultState1();
    }

    @Override
    public IBlockState setBlockState(long x, long y, long z, IBlockState blockState) {
        try {
            return blockStates[(int) ((x * width + y) * height + z)] = blockState;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
