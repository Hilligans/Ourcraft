package dev.hilligans.engine.util.interfaces;

public interface TriConsumer<T,Q,O> {

    void accept(T t, Q q, O o);

}
