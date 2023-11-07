package dev.hilligans.ourcraft.world.newworldsystem;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class EmptyContainer<T> implements IThreeDContainer<T> {

    public Long2ObjectOpenHashMap<Int2ObjectOpenHashMap<T>> container = new Long2ObjectOpenHashMap<>();

    @Override
    public synchronized T getChunk(long x, long y, long z) {
        Int2ObjectOpenHashMap<T> c = container.get(to(x,y));
        if(c == null) {
            return null;
        }
        return c.getOrDefault((int)z, null);
    }

    @Override
    public synchronized T setChunk(long x, long y, long z, T val) {
        Int2ObjectOpenHashMap<T> c = container.computeIfAbsent(to(x,y), (a) -> new Int2ObjectOpenHashMap<>());
        return c.put((int)z,val);
    }

    @Override
    public T removeChunk(long x, long y, long z) {
        Int2ObjectOpenHashMap<T> c = container.get(to(x,y));
        if(c == null) {
            return null;
        }
        return c.remove((int)z);
    }

    @Override
    public T removeChunk(long pos) {
        return null;
    }

    @Override
    public int getSize() {
        return 0;
    }

    @Override
    public void forEach(Consumer<T> consumer) {
        BiConsumer<Integer, T> biConsumer = (integer, t) -> consumer.accept(t);
        container.forEach((aLong, tInt2ObjectOpenHashMap) -> tInt2ObjectOpenHashMap.forEach(biConsumer));
    }

    public long to(long x, long y) {
        return ((y & 0xffffffffL) << 32) | (x & 0xffffffffL);
    }
}
