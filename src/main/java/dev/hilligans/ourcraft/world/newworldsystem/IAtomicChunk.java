package dev.hilligans.ourcraft.world.newworldsystem;

import dev.hilligans.ourcraft.block.blockstate.IBlockState;
import dev.hilligans.ourcraft.server.concurrent.Lock;

import java.util.function.UnaryOperator;

public interface IAtomicChunk extends IChunk {


    /**
     * @param lock a lock provided to the chunk in the event that a subchunk isn't atomic and needs to grab the lock, the lock must also be manually freed after
     */
    void setBlockStateAtomic(Lock lock, long x, long y, long z, IBlockState blockState);


    /**
     * Used to replace an old blockstate with a new one
     * useful if you want to do say a plant growing, so you can change the blockstate without having to grab the write lock
     * @param lock a lock provided to the chunk in the event that a subchunk isn't atomic and needs to grab the lock, the lock must also be manually freed after
     * @param x x pos in subchunk
     * @param y y pos in subchunk
     * @param z z pos in subchunk
     * @param expected the blockstate to replace if it still exists
     * @param to the new blockstate
     * @return true if the expected blockstate match and the block was set, false otherwise
     */
    boolean swapBlockStateAtomic(Lock lock, long x, long y, long z, IBlockState expected, IBlockState to);

    void replaceAtomic(UnaryOperator<ISubChunk> replacer);
}
