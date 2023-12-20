package dev.hilligans.ourcraft.util;

import dev.hilligans.ourcraft.GameInstance;

import java.util.concurrent.ConcurrentLinkedQueue;

public class ThreadProvider {

    public GameInstance gameInstance;
    public ConcurrentLinkedQueue<Thread> threads = new ConcurrentLinkedQueue<>();
    public volatile boolean stopped = false;
    public volatile int threadCount = 0;

    public ThreadProvider(GameInstance gameInstance) {
        this.gameInstance = gameInstance;
    }

    public Thread startVirtualThread(Runnable task) {
        Thread thread = Thread.startVirtualThread(() -> {
            Thread thr = Thread.currentThread();
            addThread(thr);

            task.run();
            threads.remove(thr);
        });

        return thread;
    }

    public synchronized void addThread(Thread thread) {
        while (stopped) {}
        threadCount++;
        threads.add(thread);
    }

    public synchronized void removeThread(Thread thread) {
        while (stopped) {}
        threadCount--;
        threads.remove(thread);
    }

    private synchronized void pause() {
        stopped = true;
    }

    private void unpause() {

    }
    public void stopTheWorld(Thread stoppingThread) throws Exception {
        pause();
        for(Thread thread : threads) {
            if(thread == stoppingThread) {
                continue;
            }
        }
    }

    public void startTheWorld(Thread thread) throws Exception {

    }
}
