package dev.hilligans.ourcraft.Util;

public interface TriConsumer<T,Q,O> {

    void accept(T t, Q q, O o);

}
