package dev.hilligans.ourcraft.world.newworldsystem;

import dev.hilligans.ourcraft.block.blockstate.IBlockState;
import dev.hilligans.ourcraft.Ourcraft;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.util.ArrayList;

public class GlobalPaletteAtomicSubChunk implements IAtomicSubChunk {

    static final VarHandle ARRAY_HANDLE = MethodHandles.arrayElementVarHandle(short[].class);
    public ArrayList<IBlockState> blockStates;
    public short[] blocks = new short[16 * 16 * 16];

    public GlobalPaletteAtomicSubChunk(ArrayList<IBlockState> blockStates) {
        this.blockStates = blockStates;
        if(blockStates.size() > Short.MAX_VALUE * 2) {
            throw new RuntimeException("Unable to use this subchunk impl");
        }
    }

    public GlobalPaletteAtomicSubChunk() {
        this(Ourcraft.GAME_INSTANCE.BLOCK_STATES);
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
        return blockStates.get((blocks[getIndex(x, y, z)]));
    }

    @Override
    public IBlockState setBlockState(IWorld world, int x, int y, int z, IBlockState blockState) {
        int id = blockState.getBlockStateID();
        blocks[getIndex(x, y, z)] = (short) id;
        return blockStates.get(id);
    }

    @Override
    public IBlockState setBlockStateAtomic(int x, int y, int z, IBlockState blockState) {
        int id = blockState.getBlockStateID();
        int index = getIndex(x, y, z);
        int v;
        do {
            v = (int) ARRAY_HANDLE.getVolatile(blocks, index);
        } while (!ARRAY_HANDLE.weakCompareAndSet(blocks, index, v, id));

        return blockStates.get(v);
    }

    @Override
    public boolean swapBlockStateAtomic(int x, int y, int z, IBlockState expected, IBlockState to) {
        int newID = to.getBlockStateID();
        int oldID = expected.getBlockStateID();
        int index = getIndex(x, y, z);
        int v;
        do {
            v = (int) ARRAY_HANDLE.getVolatile(blocks, index);
            if(v != oldID) {
                return false;
            }
        } while (!ARRAY_HANDLE.weakCompareAndSet(blocks, index, v, newID));
        return true;
    }

    public static int getIndex(int x, int y, int z) {
        return ((x * 16) + y) * 16 + z;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public ISubChunk canInsertOrGetNext(IBlockState blockState) {
        return null;
    }
}
