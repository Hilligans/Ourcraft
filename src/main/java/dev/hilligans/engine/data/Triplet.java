package dev.hilligans.engine.data;

public class Triplet<T,Q,V> {
    public T typeA;
    public Q typeB;
    public V typeC;

    public Triplet(T typeA, Q typeB, V typeC) {
        this.typeA = typeA;
        this.typeB = typeB;
        this.typeC = typeC;
    }

    public T getTypeA() {
        return typeA;
    }

    public Q getTypeB() {
        return typeB;
    }

    public V getTypeC() {
        return typeC;
    }
}
