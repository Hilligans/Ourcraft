package dev.hilligans.ourcraft.Server.Concurrent;

import dev.hilligans.ourcraft.World.World;

public abstract class TickingBase implements ITickableTask {

    public boolean running = true;
    public ParkerUnparker parkerUnparker;
    public Thread owner;

    public World world;
    public ChunkLocker chunkLocker;


    @Override
    public void stop() {
        this.running = false;
    }

    @Override
    public void start(World world, ChunkLocker chunkLocker) {
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
