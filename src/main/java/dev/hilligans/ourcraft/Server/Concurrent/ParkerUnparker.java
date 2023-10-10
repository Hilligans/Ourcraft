package dev.hilligans.ourcraft.Server.Concurrent;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;

public class ParkerUnparker {


    public AtomicInteger parkerIndex = new AtomicInteger();
    public int count;
    public volatile Thread[] parkedThreads;

    public ParkerUnparker(int count) {
        this.count = count;
        this.parkedThreads = new Thread[count];
    }

    public void waitForThreads() {
        for(int x = 0; x < count; x++) {
            while(parkedThreads[x] == null) {}
            while (parkedThreads[x].getState() != Thread.State.WAITING) {}
        }
    }

    public void unpark() {
        parkerIndex.set(0);
        for(int x = 0; x < count; x++) {
            Thread thread = parkedThreads[x];
            parkedThreads[x] = null;
            LockSupport.unpark(thread);
        }
    }

    public void park(ITickableTask task) {
        parkedThreads[parkerIndex.getAndIncrement()] = task.getThread();
        LockSupport.park(this);
    }
}
