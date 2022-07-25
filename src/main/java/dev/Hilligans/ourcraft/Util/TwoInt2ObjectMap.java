package dev.Hilligans.ourcraft.Util;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

public class TwoInt2ObjectMap<O> {

    public Int2ObjectOpenHashMap<Int2ObjectOpenHashMap<O>> map = new Int2ObjectOpenHashMap<>();

    public void put(int x, int z, O data) {
        map.computeIfAbsent(x,a -> new Int2ObjectOpenHashMap<>()).put(z,data);
    }

    public O get(int x, int z) {
        Int2ObjectOpenHashMap<O> container = map.getOrDefault(x,null);
        if(container != null) {
            return container.getOrDefault(z,null);
        }
        return null;
    }
}
