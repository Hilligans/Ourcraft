package dev.hilligans.ourcraft.resource;

public interface IAllocator<T> {

    void free(T resource);

}
