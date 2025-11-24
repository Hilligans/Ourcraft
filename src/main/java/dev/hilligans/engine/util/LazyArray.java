package dev.hilligans.engine.util;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class LazyArray<T> implements Array<T> {

    int size;
    Function<Integer, T> producer;
    BiConsumer<Integer, T> consumer;

    public LazyArray(int size, Function<Integer, T> producer, BiConsumer<Integer, T> consumer) {
        this.size = size;
        this.producer = producer;
        this.consumer = consumer;
    }

    @Override
    public int length() {
        return size;
    }

    @Override
    public T get(int index) {
        return producer.apply(index);
    }

    @Override
    public void set(int index, T element) {
        consumer.accept(index, element);
    }
}