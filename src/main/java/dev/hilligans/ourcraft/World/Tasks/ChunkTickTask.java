package dev.hilligans.ourcraft.World.Tasks;

import dev.hilligans.ourcraft.Block.Block;
import dev.hilligans.ourcraft.Block.BlockState.IBlockState;
import dev.hilligans.ourcraft.Data.Other.BlockPos;
import dev.hilligans.ourcraft.Data.Other.ChunkPos;
import dev.hilligans.ourcraft.Server.Concurrent.*;
import dev.hilligans.ourcraft.World.NewWorldSystem.IChunk;
import dev.hilligans.ourcraft.World.NewWorldSystem.IMethodResult;
import dev.hilligans.ourcraft.World.NewWorldSystem.ISubChunk;
import dev.hilligans.ourcraft.World.NewWorldSystem.IWorld;
import dev.hilligans.ourcraft.World.World;

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
                    IBlockState blockState = iSubChunk.getBlockState(x, y, z);
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