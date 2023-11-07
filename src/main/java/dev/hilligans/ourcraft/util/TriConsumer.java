package dev.hilligans.ourcraft.util;

public interface TriConsumer<T,Q,O> {

    void accept(T t, Q q, O o);

}
