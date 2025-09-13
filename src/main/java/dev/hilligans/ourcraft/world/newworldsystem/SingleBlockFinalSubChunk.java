package dev.hilligans.ourcraft.world.newworldsystem;

import dev.hilligans.engine.engine.EngineImplementationException;
import dev.hilligans.ourcraft.block.blockstate.IBlockState;

public class SingleBlockFinalSubChunk implements IAtomicSubChunk {

    public final IBlockState blockState;
    final short width;
    final short height;
    public static final boolean CHECKED = false;

    public SingleBlockFinalSubChunk(IBlockState blockState) {
        this(blockState, 16, 16);
    }

    public SingleBlockFinalSubChunk(IBlockState blockState, int width, int height) {
        this.blockState = blockState;
        this.width = (short) width;
        this.height = (short) height;
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
    public IBlockState getBlockState(IWorld world, int x, int y, int z) {
        return blockState;
    }

    @Override
    public IBlockState setBlockState(IWorld world, int x, int y, int z, IBlockState blockState) {
        if(CHECKED && blockState != this.blockState) {
            throw new EngineImplementationException("canInsertOrGetNext will always return a new subchunk when states dont match so a blockstate set should never be attempted here");
        }
        return this.blockState;
    }

    @Override
    public boolean isEmpty() {
        return blockState.getBlock().blockProperties.airBlock;
    }

    @Override
    public ISubChunk canInsertOrGetNext(IBlockState blockState) {
        if(blockState == this.blockState) {
            return null;
        }
        return new PalettedSubChunk(this);
    }

    @Override
    public IBlockState setBlockStateAtomic(int x, int y, int z, IBlockState blockState) {
        if(CHECKED && blockState != this.blockState) {
            throw new EngineImplementationException("canInsertOrGetNext will always return a new subchunk when states dont match so a blockstate set should never be attempted here");
        }
        return this.blockState;
    }

    @Override
    public boolean swapBlockStateAtomic(int x, int y, int z, IBlockState expected, IBlockState to) {
        if(CHECKED && to != this.blockState) {
            throw new EngineImplementationException("canInsertOrGetNext will always return a new subchunk when states dont match so a blockstate set should never be attempted here");
        }
        return expected == to && to == this.blockState;
    }
}
