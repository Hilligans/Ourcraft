package dev.hilligans.ourcraft.Resource;

public class EmptyAllocator<T> implements IAllocator<T> {

    public static final EmptyAllocator<?> INSTANCE = new EmptyAllocator<>();

    @Override
    public void free(Object resource) {
    }
}
