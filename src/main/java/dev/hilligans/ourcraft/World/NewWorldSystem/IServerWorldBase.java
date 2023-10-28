package dev.hilligans.ourcraft.World.NewWorldSystem;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Future;
import java.util.function.Consumer;

public interface IServerWorldBase extends IServerWorld {

    @Override
    default void queuePostTickEvent(Future<Consumer<IServerWorld>> future) {
        getPostTickQueue().add(future);
    }

    @Override
    default void processPostTickEvents(Future<Consumer<IServerWorld>> runnableFuture) {
        ConcurrentLinkedQueue<Future<Consumer<IServerWorld>>> futures = getPostTickQueue();
        while (!futures.isEmpty()) {
            try {
                futures.remove().get().accept(this);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    ConcurrentLinkedQueue<Future<Consumer<IServerWorld>>> getPostTickQueue();

}
