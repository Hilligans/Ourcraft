package dev.hilligans.engine.util;

import java.util.function.Supplier;

public class StableValue<T> {

    private T value;
    private final Supplier<T> supplier;

    private StableValue(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    public T get() {
        if(value == null) {
            value = supplier.get();
        }
        return value;
    }

    public static <T> StableValue<T>supplier(Supplier<T> supplier) {
        return new StableValue<>(supplier);
    }
}
