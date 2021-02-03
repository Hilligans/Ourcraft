package Hilligans.Data.Primitives;

public class TripleTypeWrapper<T,Q,V> {
    T typeA;
    Q typeB;
    V typeC;

    public TripleTypeWrapper(T typeA, Q typeB,V typeC) {
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
