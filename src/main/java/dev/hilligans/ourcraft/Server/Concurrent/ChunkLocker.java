package dev.hilligans.ourcraft.Server.Concurrent;

import dev.hilligans.ourcraft.Data.Other.ChunkPos;
import dev.hilligans.ourcraft.World.NewWorldSystem.EmptyContainer;
import dev.hilligans.ourcraft.World.NewWorldSystem.IThreeDContainer;

import java.util.HashSet;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.LockSupport;

public class ChunkLocker {

    public IThreeDContainer<ChunkLock> chunkLocks = new EmptyContainer<>();

    public ChunkLocker() {

    }

    public void release(Lock lock) {
        HashSet<Lock> locks = new HashSet<>();
        lock.myLock.set(false);
        for(ChunkLock chunkLock : lock.chunkLocks) {
            locks.addAll(chunkLock.waitingLocks);
        }
        for(Lock lock1 : locks) {
            lock1.notifyOfUnlock();
        }
    }

    public void acquire(Lock lock) {
        if(!tryAcquire(lock)) {
            if(lock.notifications.get() == 2) {
                if(tryReAcquire(lock)) {
                    return;
                }
            }
            lock.parked = true;
            lock.notifications.set(-1);
            LockSupport.park(this);
            //when we finally get unparked we are guaranteed to have gotten out lock
            lock.notifications.set(-2);
            for (ChunkLock chunkLock : lock.chunkLocks) {
                chunkLock.waitingLocks.remove(lock);
            }
        }
    }

    public boolean tryAcquire(Lock lock) {
        AtomicBoolean atomicBoolean = new AtomicBoolean(true);
        int x = 0;
        lock.thread = Thread.currentThread();
        lock.chunkLocker = this;
        for (ChunkPos chunkPos : lock.chunkPositions) {
            lock.chunkLocks[x] = chunkLocks.getChunk(chunkPos.chunkX, chunkPos.chunkY, chunkPos.chunkZ);
            lock.chunkLocks[x].waitingLocks.add(lock);
            x++;
        }
        lock.notifications.set(1);
        for (ChunkLock chunkLock : lock.chunkLocks) {
            if (!chunkLock.tryAcquire(atomicBoolean)) {
                atomicBoolean.set(false);
                return false;
            }
        }
        lock.myLock = atomicBoolean;
        for (ChunkLock chunkLock : lock.chunkLocks) {
            chunkLock.waitingLocks.remove(lock);
        }
        lock.hasAllLocks = true;
        return true;
    }

    //called when release locks
    public boolean tryReAcquire(Lock lock) {
        AtomicBoolean atomicBoolean = new AtomicBoolean(true);
        for (ChunkPos chunkPos : lock.chunkPositions) {
            if (!chunkLocks.getChunk(chunkPos.chunkX, chunkPos.chunkY, chunkPos.chunkZ).tryAcquire(atomicBoolean)) {
                atomicBoolean.set(false);
                return false;
            }
        }
        lock.myLock = atomicBoolean;
        lock.hasAllLocks = true;
        return true;
    }
}
