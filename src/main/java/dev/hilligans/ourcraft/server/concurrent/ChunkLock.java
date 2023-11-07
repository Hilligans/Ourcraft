package dev.hilligans.ourcraft.server.concurrent;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class ChunkLock {

    public long chunkX;
    public long chunkY;
    public long chunkZ;

    public volatile AtomicBoolean getLock = new AtomicBoolean();
    public ConcurrentLinkedQueue<Lock> waitingLocks = new ConcurrentLinkedQueue<>();

    public ChunkLock(long x, long y, long z) {
        this.chunkX = x;
        this.chunkY = y;
        this.chunkZ = z;
        this.getLock.set(false);
    }

    public boolean isOwned() {
        return getLock.get();
    }

    public boolean tryAcquire(AtomicBoolean atomicBoolean) {
        boolean res = getLock.compareAndSet(false, true);
        if(res) {
            getLock = atomicBoolean;
        }
        return res;
    }
}
