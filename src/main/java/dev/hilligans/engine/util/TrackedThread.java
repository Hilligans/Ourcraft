package dev.hilligans.engine.util;

public class TrackedThread extends Thread {

    public TrackedThread(ThreadGroup group, Runnable task, String name, long stackSize) {
        super(group, task, name, stackSize);
    }

    @Override
    public void run() {
        super.run();
        ThreadProvider.unmapThread(this);
    }
}
