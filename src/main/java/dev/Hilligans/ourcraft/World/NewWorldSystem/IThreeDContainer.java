package dev.Hilligans.ourcraft.World.NewWorldSystem;

import java.util.function.Consumer;

public interface IThreeDContainer<T> {

    T getChunk(long x, long y, long z);

    T setChunk(long x, long y, long z, T val);

    T removeChunk(long x, long y, long z);

    T removeChunk(long pos);

    int getSize();

    void forEach(Consumer<T> consumer);

}
