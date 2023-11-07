package dev.hilligans.ourcraft.world.modifications.set;

import dev.hilligans.ourcraft.block.blockstate.IBlockState;
import dev.hilligans.ourcraft.data.other.BlockPos;
import dev.hilligans.ourcraft.server.concurrent.Lock;
import dev.hilligans.ourcraft.world.modifications.IWorldModification;
import dev.hilligans.ourcraft.world.newworldsystem.IAtomicChunk;
import dev.hilligans.ourcraft.world.newworldsystem.IChunk;
import dev.hilligans.ourcraft.world.newworldsystem.SingleBlockFinalSubChunk;

public class SetSingleModification implements IWorldModification {

    public final BlockPos min;
    public final BlockPos max;
    public final IBlockState blockState;

    public SetSingleModification(BlockPos min, BlockPos max, IBlockState blockState) {
        this.min = min;
        this.max = max;
        this.blockState = blockState;
    }

    @Override
    public void apply(Lock lock, IChunk chunk) {
        SingleBlockFinalSubChunk singleBlockFinalSubChunk = new SingleBlockFinalSubChunk(blockState);
        if(lock.hasLock(chunk.getChunkPos())) {
            if(isWholeInside(chunk)) {
                chunk.replace(a -> singleBlockFinalSubChunk);
            } else {
                grabAndSet(lock, chunk);

            }
            return;
        }
        if(chunk instanceof IAtomicChunk atomicChunk) {
            if(isWholeInside(chunk)) {
                atomicChunk.replaceAtomic(a -> singleBlockFinalSubChunk);
            } else {
                grabAndSet(lock, chunk);
            }
            return;
        }
        try(Lock l = lock.acquire(chunk.getChunkPos())) {
            if(isWholeInside(chunk)) {
                chunk.replace(a -> singleBlockFinalSubChunk);
            } else {
                grabAndSet(lock, chunk);

            }
        }
    }

    private void grabAndSet(Lock lock, IChunk chunk) {
        lock.acquire(chunk.getChunkPos());
        long endX = Math.min(getMaxX(), chunk.getBlockMaxX()) + 1;
        long endY = Math.min(getMaxY(), chunk.getBlockMaxY()) + 1;
        long endZ = Math.min(getMaxZ(), chunk.getBlockMaxZ()) + 1;
        set(chunk, Math.max(getMinX(), chunk.getBlockX()), Math.max(getMinY(), chunk.getBlockY()), Math.max(getMinZ(), chunk.getBlockZ()), endX, endY, endZ);
    }

    private void set(IChunk c, long startX, long startY, long startZ, long endX, long endY, long endZ) {
        for(long x = startX; x < endX; x++) {
            for(long y = startY; y < endY; y++) {
                for(long z = startZ; z < endZ; z++) {
                    c.setBlockState(x, y, z, blockState);
                }
            }
        }
    }

    @Override
    public BlockPos getMin() {
        return min;
    }

    @Override
    public BlockPos getMax() {
        return max;
    }
}
