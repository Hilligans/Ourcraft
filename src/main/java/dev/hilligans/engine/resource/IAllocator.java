package dev.hilligans.engine.resource;

public interface IAllocator<T> {

    void free(T resource);

}
