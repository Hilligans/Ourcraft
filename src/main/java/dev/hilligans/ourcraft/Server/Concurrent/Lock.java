package dev.hilligans.ourcraft.Server.Concurrent;

import dev.hilligans.ourcraft.Data.Other.ChunkPos;

import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;

public class Lock {

    public boolean hasAllLocks = false;

    public AtomicBoolean myLock = new AtomicBoolean(true);
    public Thread thread;
    public ChunkLocker chunkLocker;
    ChunkPos[] chunkPositions;
    ChunkLock[] chunkLocks;

    public volatile boolean parked = false;
    public AtomicInteger notifications = new AtomicInteger();


    public Lock(ChunkPos... positions) {
        this.chunkPositions = positions;
        Arrays.sort(this.chunkPositions, Comparator.comparingLong((ChunkPos o) -> o.chunkX).thenComparingLong(o -> o.chunkY).thenComparingLong(o -> o.chunkZ));
        chunkLocks = new ChunkLock[chunkPositions.length];
    }

    public void notifyOfUnlock() {
        int res = notifications.compareAndExchangeAcquire(1, 2);
        if(res == -1) {
            if(parked) {
                if (chunkLocker.tryReAcquire(this)) {
                    parked = false;
                    LockSupport.unpark(thread);
                }
            }
        }
    }

    public void releaseLock() {
        myLock.set(false);
    }

    public boolean hasAllLocks() {
        return hasAllLocks;
    }


    public void acquire(long... positions) {}

    public void acquire() {}

    public void release() {}

}
