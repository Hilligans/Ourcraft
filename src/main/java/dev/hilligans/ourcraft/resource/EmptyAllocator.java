package dev.hilligans.ourcraft.resource;

public class EmptyAllocator<T> implements IAllocator<T> {

    public static final EmptyAllocator<?> INSTANCE = new EmptyAllocator<>();

    @Override
    public void free(Object resource) {
    }
}
