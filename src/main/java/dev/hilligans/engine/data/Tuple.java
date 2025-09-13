package dev.hilligans.engine.data;

import java.util.function.Supplier;

public class Tuple<T,Q> implements Supplier<T> {

    public T typeA;
    public Q typeB;

    public Tuple(T typeA, Q typeB) {
        this.typeA = typeA;
        this.typeB = typeB;
    }

    public Tuple() {

    }

    public T getTypeA() {
        return typeA;
    }

    public Q getTypeB() {
        return typeB;
    }

    public boolean has() {
        return typeA != null && typeB != null;
    }

    @Override
    public String toString() {
        return "<" + typeA + "," + typeB + ">";
    }

    @Override
    public T get() {
        return getTypeA();
    }
}
