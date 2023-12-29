package dev.hilligans.ourcraft.world.tasks;

import dev.hilligans.ourcraft.block.Block;
import dev.hilligans.ourcraft.block.blockstate.IBlockState;
import dev.hilligans.ourcraft.data.other.BlockPos;
import dev.hilligans.ourcraft.data.other.ChunkPos;
import dev.hilligans.ourcraft.server.concurrent.*;
import dev.hilligans.ourcraft.world.newworldsystem.IChunk;
import dev.hilligans.ourcraft.world.newworldsystem.IMethodResult;
import dev.hilligans.ourcraft.world.newworldsystem.ISubChunk;

import java.util.Random;
import java.util.function.Consumer;

public class ChunkTickTask extends TickingBase implements IMethodResult {

    public IChunk chunk;
    public Random random;
    public int randomTickCount = 3;

    public ChunkTickTask(IChunk chunk) {
        this.chunk = chunk;
        this.random = new Random();
    }

    @Override
    public void tick() {

        ChunkTickTask instance = this;
        //we make the lock with all the chunk positions we want
        Lock lock = new Lock(chunkLocker, new ChunkPos(chunk.getX(), chunk.getY(), chunk.getZ()));
        chunk.forEach(new Consumer<ISubChunk>() {
            @Override
            public void accept(ISubChunk iSubChunk) {
                for(int i = 0; i < randomTickCount; i++) {
                    int width = iSubChunk.getWidth();
                    int height = iSubChunk.getHeight();
                    int pos = random.nextInt(width * width * height);
                    int z = pos % width;
                    pos /= width;
                    int y = pos % height;
                    int x = pos / width;
                    IBlockState blockState = iSubChunk.getBlockState(world, x, y, z);
                    blockState.getBlock().randomTick(lock, instance, blockState, chunk, world, new BlockPos(x, y, z), random);
                }
            }
        });

        //we can then acquire it when we need it
        //lock.acquire();
        //and release it when we no longer need it
       // lock.release();
    }

    @Override
    public void queueUpdate(int x, int y, int z) {

    }

    @Override
    public void queueUpdate(BlockPos pos) {

    }

    @Override
    public void queueSixUpdates(int x, int y, int z) {

    }

    @Override
    public void queueSixUpdates(BlockPos pos) {

    }

    @Override
    public void scheduleTick(int x, int y, int z, Block block, int delay) {

    }

    @Override
    public void scheduleTick(BlockPos pos, Block block, int delay) {

    }
}