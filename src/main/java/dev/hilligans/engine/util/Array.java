package dev.hilligans.engine.util;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public interface Array<T> extends Iterable<T> {

    int length();
    T get(int index);
    void set(int index, T value);

    @Override
    default @NotNull Iterator<T> iterator() {
        return new Iterator<>() {

            int index = 0;

            @Override
            public boolean hasNext() {
                return index < length();
            }

            @Override
            public T next() {
                return get(index++);
            }
        };
    }
}
