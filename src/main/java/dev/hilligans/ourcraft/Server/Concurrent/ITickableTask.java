package dev.hilligans.ourcraft.Server.Concurrent;

import dev.hilligans.ourcraft.World.World;

public interface ITickableTask extends Runnable {

    void stop();

    void start(World world, ChunkLocker chunkLocker);

    void setParker(ParkerUnparker parkerUnparker);

    void tick();

    Thread getThread();

}
