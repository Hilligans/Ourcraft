package dev.hilligans.ourcraft.World.NewWorldSystem;

import dev.hilligans.ourcraft.Block.BlockState.IBlockState;
import dev.hilligans.ourcraft.Server.Concurrent.IAtomicSubChunk;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.util.ArrayList;

public class GlobalPaletteAtomicSubChunk implements IAtomicSubChunk {

    static final VarHandle ARRAY_HANDLE = MethodHandles.arrayElementVarHandle(int[].class);
    public ArrayList<IBlockState> blockStates;
    public int[] blocks = new int[16 * 16 * 16 / 4];

    public GlobalPaletteAtomicSubChunk(ArrayList<IBlockState> blockStates) {
        this.blockStates = blockStates;
        if(blockStates.size() > Short.MAX_VALUE * 2) {
            throw new RuntimeException("Unable to use this subchunk impl");
        }
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
        return blockStates.get((blocks[getIndex(x, y, z)] >> 16 * (z & 0b11)) & 0xFFFF);
    }

    @Override
    public IBlockState setBlockState(int x, int y, int z, IBlockState blockState) {
        int shift = 16 * (z & 0b11);
        int id = blockState.getBlockStateID() << shift;
        int and = 0xFFFF << shift;
        int index = getIndex(x, y, z);
        int v = blocks[index];
        blocks[index] = v & and | id;
        return blockStates.get((v >> shift) & 0xFFFF);
    }

    @Override
    public IBlockState setBlockStateAtomic(int x, int y, int z, IBlockState blockState) {
        int shift = 16 * (z & 0b11);
        int id = blockState.getBlockStateID() << shift;
        int and = 0xFFFF << shift;
        int index = getIndex(x, y, z);
        int v;
        do {
            v = (int) ARRAY_HANDLE.getVolatile(blocks, index);
        } while (!ARRAY_HANDLE.weakCompareAndSet(blocks, index, v, v & and | id));

        return blockStates.get((v >> shift) & 0xFFFF);
    }

    @Override
    public boolean swapBlockStateAtomic(int x, int y, int z, IBlockState expected, IBlockState to) {
        int shift = 16 * (z & 0b11);
        int newID = to.getBlockStateID() << shift;
        int oldID = expected.getBlockStateID() << shift;
        int and = 0xFFFF << shift;
        int index = getIndex(x, y, z);
        int v;
        do {
            v = (int) ARRAY_HANDLE.getVolatile(blocks, index);
            if((v & and) != oldID) {
                return false;
            }
        } while (!ARRAY_HANDLE.weakCompareAndSet(blocks, index, v, v & and | newID));
        return true;
    }

    public static int getIndex(int x, int y, int z) {
        return ((x * 16) + y) * 4 + (z >> 2);
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
