package dev.hilligans.ourcraft.world.newworldsystem;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;

public interface IServerWorldBase extends IServerWorld {

    @Override
    default void queuePostTickEvent(Consumer<IServerWorld> consumer) {
        getPostTickQueue().add(consumer);
    }

    @Override
    default void processPostTickEvents() {
        ConcurrentLinkedQueue<Consumer<IServerWorld>> futures = getPostTickQueue();
        while (!futures.isEmpty()) {
            try {
                futures.remove().accept(this);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    ConcurrentLinkedQueue<Consumer<IServerWorld>> getPostTickQueue();
}
