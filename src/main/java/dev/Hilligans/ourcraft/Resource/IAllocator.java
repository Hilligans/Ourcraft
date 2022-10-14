package dev.Hilligans.ourcraft.Resource;

public interface IAllocator<T> {

    void free(T resource);

}
