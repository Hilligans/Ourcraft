package dev.hilligans.ourcraft.server.concurrent;

import dev.hilligans.ourcraft.world.newworldsystem.IWorld;

public interface ITickableTask extends Runnable {

    void stop();

    void start(IWorld world, ChunkLocker chunkLocker);

    void setParker(ParkerUnparker parkerUnparker);

    void tick();

    Thread getThread();

}
