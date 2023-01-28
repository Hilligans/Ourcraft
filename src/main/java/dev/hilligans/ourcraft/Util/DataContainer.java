package dev.hilligans.ourcraft.Util;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;

import java.util.function.Consumer;

public class DataContainer<T> implements IDataContainer<T> {

    public Long2ObjectOpenHashMap<Int2ObjectOpenHashMap<T>> values = new Long2ObjectOpenHashMap<>();
    public int size = 0;

    @Override
    public T get(int x, int y, int z) {
        try {
            Int2ObjectOpenHashMap<T> yMap = values.getOrDefault(toLong(x,z), null);
            if (yMap != null) {
                return yMap.getOrDefault(y, null);
            }
        } catch(Exception e){}
        return null;
    }

    @Override
    public T set(int x, int y, int z, T val) {
        Int2ObjectOpenHashMap<T> yMap = values.computeIfAbsent(toLong(x,z),a -> new Int2ObjectOpenHashMap<>());
        T oldVal = yMap.put(y,val);
        if(oldVal == null) {
            size++;
        }
        return oldVal;
    }

    @Override
    public T remove(int x, int y, int z) {
        Int2ObjectOpenHashMap<T> yMap = values.getOrDefault(toLong(x,z),null);
        if(yMap != null) {
            T oldVal = yMap.remove(y);
            if(oldVal != null) {
                size--;
            }
            return oldVal;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public void forEach(Consumer<T> consumer) {

        for(long val : values.keySet()) {
            Int2ObjectOpenHashMap<T> yMap = values.getOrDefault(val,null);
            yMap.forEach((integer, value) -> consumer.accept(value));
        }
    }

    public long toLong(long x, long z) {
        return x << 32 | z;
    }
}
