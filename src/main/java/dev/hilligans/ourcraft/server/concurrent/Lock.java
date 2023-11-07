package dev.hilligans.ourcraft.server.concurrent;

import dev.hilligans.ourcraft.data.other.ChunkPos;

import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;

public class Lock implements AutoCloseable {

    public boolean hasAllLocks = false;

    public volatile AtomicBoolean myLock = new AtomicBoolean(true);
    public Thread thread;
    public ChunkLocker chunkLocker;
    ChunkPos[] chunkPositions;
    ChunkLock[] chunkLocks;

    public volatile boolean parked = false;
    public AtomicInteger notifications = new AtomicInteger();


    public Lock(ChunkLocker chunkLocker, ChunkPos... positions) {
        this.chunkPositions = positions;
        Arrays.sort(this.chunkPositions, Comparator.comparingLong((ChunkPos o) -> o.chunkX).thenComparingLong(o -> o.chunkY).thenComparingLong(o -> o.chunkZ));
        chunkLocks = new ChunkLock[chunkPositions.length];
        this.chunkLocker = chunkLocker;
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

    public boolean hasLock(ChunkPos chunkPos) {
        if(!hasAllLocks) {
            return false;
        }
        for(ChunkPos chunkPos1 : chunkPositions) {
            if(chunkPos1.equals(chunkPos)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasAllLocks() {
        return hasAllLocks;
    }

    public void acquire(long... positions) {}

    public Lock acquire(ChunkPos chunkPos) {
        for(ChunkPos chunkPos1 : chunkPositions) {
            if(chunkPos1.equals(chunkPos)) {
                acquire();
                return this;
            }
        }
        ChunkPos[] chunkPosList = new ChunkPos[chunkPositions.length + 1];
        System.arraycopy(chunkPositions,0, chunkPosList, 0, chunkPositions.length);
        chunkPosList[chunkPositions.length] = chunkPos;
        this.chunkPositions = chunkPosList;
        return this;
    }

    public void acquire() {
        chunkLocker.acquire(this);
    }

    public void release() {
        chunkLocker.release(this);
    }

    @Override
    public void close() {
        release();
    }
}
