package dev.hilligans.ourcraft.util;

import dev.hilligans.ourcraft.GameInstance;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class NamedThreadFactory implements ThreadFactory {
    private static final AtomicInteger poolNumber = new AtomicInteger(1);
    private final ThreadGroup group;
    private final AtomicInteger threadNumber = new AtomicInteger(1);
    private final String namePrefix;
    private final GameInstance gameInstance;

    public NamedThreadFactory(String name) {
        SecurityManager s = System.getSecurityManager();
        this.gameInstance = null;
        group = (s != null) ? s.getThreadGroup() :
                Thread.currentThread().getThreadGroup();
        namePrefix = name + "-pool-" +
                poolNumber.getAndIncrement() +
                "-thread";
    }

    public NamedThreadFactory(String name, GameInstance gameInstance) {
        this.gameInstance = gameInstance;
        SecurityManager s = System.getSecurityManager();
        group = (s != null) ? s.getThreadGroup() :
                Thread.currentThread().getThreadGroup();
        namePrefix = name + "-pool-" +
                poolNumber.getAndIncrement() +
                "-thread";
    }

    public Thread newThread(Runnable r) {
        Thread t = new Thread(group, r,
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