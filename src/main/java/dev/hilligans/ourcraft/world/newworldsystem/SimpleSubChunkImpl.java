package dev.hilligans.ourcraft.world.newworldsystem;

import dev.hilligans.ourcraft.block.blockstate.IBlockState;
import dev.hilligans.ourcraft.block.Blocks;

public class SimpleSubChunkImpl implements ISubChunk {

    public IBlockState[] blockStates;

    public int width;
    public int height;

    public SimpleSubChunkImpl(int width, int height) {
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
    public IBlockState getBlockState(int x, int y, int z) {
        if(blockStates == null) {
            return Blocks.AIR.getDefaultState();
        }
        try {
            IBlockState b =  blockStates[(int) ((x * width + y) * height + z)];
            return b == null ? Blocks.AIR.getDefaultState() : b;
        } catch (Exception e) {
           // e.printStackTrace();
        }
        return Blocks.AIR.getDefaultState();
    }

    @Override
    public IBlockState setBlockState(int x, int y, int z, IBlockState blockState) {
        if(blockStates == null) {
            if(blockState.getBlock() != Blocks.AIR) {
                blockStates = new IBlockState[width * height * width];
            } else {
                return blockState;
            }
        }
        try {
            return blockStates[(int) ((x * width + y) * height + z)] = blockState;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean isEmpty() {
        return blockStates == null;
    }

    @Override
    public ISubChunk canInsertOrGetNext(IBlockState blockState) {
        return null;
    }
}
