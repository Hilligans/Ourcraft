package dev.hilligans.ourcraft.Server.Concurrent;

import dev.hilligans.ourcraft.World.NewWorldSystem.IWorld;
import dev.hilligans.ourcraft.World.World;

public interface ITickableTask extends Runnable {

    void stop();

    void start(IWorld world, ChunkLocker chunkLocker);

    void setParker(ParkerUnparker parkerUnparker);

    void tick();

    Thread getThread();

}
