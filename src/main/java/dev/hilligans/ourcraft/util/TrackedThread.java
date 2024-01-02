package dev.hilligans.ourcraft.util;

import dev.hilligans.ourcraft.GameInstance;
import dev.hilligans.ourcraft.util.ThreadProvider;

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
