package dev.hilligans.ourcraft.server.concurrent;

import dev.hilligans.ourcraft.world.newworldsystem.IWorld;

public abstract class TickingBase implements ITickableTask {

    public boolean running = true;
    public ParkerUnparker parkerUnparker;
    public Thread owner;

    public IWorld world;
    public ChunkLocker chunkLocker;


    @Override
    public void stop() {
        this.running = false;
    }

    @Override
    public void start(IWorld world, ChunkLocker chunkLocker) {
        this.world = world;
        this.chunkLocker = chunkLocker;
        owner = Thread.startVirtualThread(this);
    }

    @Override
    public void setParker(ParkerUnparker parkerUnparker) {
        this.parkerUnparker = parkerUnparker;
    }

    @Override
    public void run() {
        while (running) {
            tick();
            parkerUnparker.park(this);
        }
    }

    @Override
    public Thread getThread() {
        return owner;
    }
}
