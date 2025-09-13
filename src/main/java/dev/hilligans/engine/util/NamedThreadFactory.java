package dev.hilligans.engine.util;

import dev.hilligans.engine.GameInstance;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class NamedThreadFactory implements ThreadFactory {
    private static final AtomicInteger poolNumber = new AtomicInteger(1);
    private final ThreadGroup group;
    private final AtomicInteger threadNumber = new AtomicInteger(1);
    private final String namePrefix;
    private final GameInstance gameInstance;

    public NamedThreadFactory(String name) {
        this.gameInstance = null;
        Thread.currentThread().getThreadGroup();
        group = Thread.currentThread().getThreadGroup();
        namePrefix = name + "-pool-" +
                poolNumber.getAndIncrement() +
                "-thread";
    }

    public NamedThreadFactory(String name, GameInstance gameInstance) {
        this.gameInstance = gameInstance;
        group = Thread.currentThread().getThreadGroup();
        namePrefix = name + "-pool-" +
                poolNumber.getAndIncrement() +
                "-thread";
    }

    public Thread newThread(Runnable r) {
        Thread t = new TrackedThread(group, r,
                namePrefix + threadNumber.getAndIncrement(),
                0);
        if(gameInstance != null) {
            gameInstance.THREAD_PROVIDER.addThread(t);
        }
        if (t.isDaemon())
            t.setDaemon(true);
        if (t.getPriority() != Thread.NORM_PRIORITY)
            t.setPriority(Thread.NORM_PRIORITY);
        return t;
    }
}