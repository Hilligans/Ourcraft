package dev.hilligans.ourcraft.util;

import dev.hilligans.ourcraft.GameInstance;
import dev.hilligans.ourcraft.Ourcraft;

import java.util.concurrent.*;

public class ThreadProvider {

    public static final ConcurrentHashMap<Thread, GameInstance> THREAD_MAP = new ConcurrentHashMap<>();
    public ExecutorService EXECUTOR;
    public GameInstance gameInstance;
    public ConcurrentLinkedQueue<Thread> threads = new ConcurrentLinkedQueue<>();
    public volatile boolean stopped = false;
    public volatile int threadCount = 0;

    public ThreadProvider(GameInstance gameInstance) {
        this.gameInstance = gameInstance;
        int threads = Math.max(2, Runtime.getRuntime().availableProcessors()/2);
        this.EXECUTOR = Executors.newFixedThreadPool(threads, new NamedThreadFactory(STR."game instance \{gameInstance.getUniqueID()} executor", gameInstance));
    }

    public Thread startVirtualThread(Runnable task, String name) {
        return Thread.startVirtualThread(() -> {
            Thread thr = Thread.currentThread();
            thr.setName(name);
            addThread(thr);
            task.run();
            threads.remove(thr);
        });
    }

    public void execute(Runnable runnable) {
        if(!EXECUTOR.isShutdown()) {
            EXECUTOR.execute(runnable);
        }
    }

    public Future<?> submit(Runnable runnable) {
        if(!EXECUTOR.isShutdown()) {
            return EXECUTOR.submit(runnable);
        }
        return null;
    }

    public synchronized void addThread(Thread thread) {
        while (stopped) {}
        threadCount++;
        mapThread(thread, gameInstance);
        threads.add(thread);
    }

    public synchronized void removeThread(Thread thread) {
        while (stopped) {}
        threadCount--;
        unmapThread(thread);
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

    public static void mapThread(Thread thread, GameInstance gameInstance) {
        THREAD_MAP.put(thread, gameInstance);
    }

    public static void unmapThread(Thread thread) {
        THREAD_MAP.remove(thread);
    }

    public static GameInstance getCurrentGameInstance() {
        return THREAD_MAP.get(Thread.currentThread());
    }
}
