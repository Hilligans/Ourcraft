package dev.hilligans.ourcraft.Util;

import java.util.function.Consumer;

public interface IDataContainer<T> {

    T get(int x, int y, int z);

    T set(int x, int y, int z, T val);

    T remove(int x, int y, int z);

    int getSize();

    void forEach(Consumer<T> consumer);

}
