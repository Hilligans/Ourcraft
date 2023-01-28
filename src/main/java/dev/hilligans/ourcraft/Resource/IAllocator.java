package dev.hilligans.ourcraft.Resource;

public interface IAllocator<T> {

    void free(T resource);

}
