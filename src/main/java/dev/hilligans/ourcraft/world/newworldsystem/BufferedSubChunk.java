package dev.hilligans.ourcraft.world.newworldsystem;

import dev.hilligans.ourcraft.block.blockstate.IBlockState;

import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.util.ArrayList;

public class BufferedSubChunk implements ISubChunk {

    public MemorySegment memorySegment;
    public ArrayList<IBlockState> blockStatePalette;

    public BufferedSubChunk(MemorySegment memorySegment, ArrayList<IBlockState> blockStatePalette) {
        this.memorySegment = memorySegment;
        this.blockStatePalette = blockStatePalette;
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
    public IBlockState getBlockState(IWorld world, int x, int y, int z) {
        return blockStatePalette.get(memorySegment.get(ValueLayout.JAVA_SHORT, getIndex(x, y, z)));
    }

    @Override
    public IBlockState setBlockState(IWorld world, int x, int y, int z, IBlockState blockState) {
        IBlockState old = getBlockState(world, x, y, z);
        memorySegment.set(ValueLayout.JAVA_SHORT, getIndex(x,y,z), (short)blockState.getBlockStateID());
        return old;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    public static int getIndex(int x, int y, int z) {
        return ((x * 16) + y) * 16 + z;
    }

    @Override
    public ISubChunk canInsertOrGetNext(IBlockState blockState) {
        return null;
    }
}
