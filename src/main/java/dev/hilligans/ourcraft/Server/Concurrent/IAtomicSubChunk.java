package dev.hilligans.ourcraft.Server.Concurrent;

import dev.hilligans.ourcraft.Block.BlockState.IBlockState;
import dev.hilligans.ourcraft.World.NewWorldSystem.ISubChunk;

public interface IAtomicSubChunk extends ISubChunk {

    IBlockState setBlockStateAtomic(int x, int y, int z, IBlockState blockState);


    /**
     * Used to replace an old blockstate with a new one
     * useful if you want to do say a plant growing, so you can change the blockstate without having to grab the write lock
     * @param x x pos in subchunk
     * @param y y pos in subchunk
     * @param z z pos in subchunk
     * @param expected the blockstate to replace if it still exists
     * @param to the new blockstate
     * @return true if the expected blockstate match and the block was set, false otherwise
     */
    boolean swapBlockStateAtomic(int x, int y, int z, IBlockState expected, IBlockState to);
}
