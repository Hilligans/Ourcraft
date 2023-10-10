package dev.hilligans.ourcraft.Server.Tasks;

import dev.hilligans.ourcraft.Data.Other.ChunkPos;
import dev.hilligans.ourcraft.Server.Concurrent.ITickableTask;
import dev.hilligans.ourcraft.Server.Concurrent.Lock;
import dev.hilligans.ourcraft.Server.Concurrent.TickingBase;
import dev.hilligans.ourcraft.World.NewWorldSystem.IChunk;

public class ChunkTask extends TickingBase {

    public IChunk chunk;

    public ChunkTask(IChunk chunk) {
        this.chunk = chunk;
    }



    @Override
    public void tick() {
        //we make the lock with all the chunk positions we want
        Lock lock = new Lock(chunkLocker, new ChunkPos(chunk.getX(), chunk.getY(), chunk.getZ()));
        //we can then acquire it when we need it
        lock.acquire();
        //and release it when we no longer need it
        lock.release();
    }
}
