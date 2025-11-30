package dev.hilligans.engine.data;

public class Quadruplet<T,Q,V,R> {
    public T typeA;
    public Q typeB;
    public V typeC;
    public R typeD;

    public Quadruplet(T typeA, Q typeB, V typeC, R typeD) {
        this.typeA = typeA;
        this.typeB = typeB;
        this.typeC = typeC;
        this.typeD = typeD;
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

    public R getTypeD() {
        return typeD;
    }
}
