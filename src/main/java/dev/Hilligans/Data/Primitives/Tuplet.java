package dev.Hilligans.Data.Primitives;

public class Tuplet<T,Q> {

    public T typeA;
    public Q typeB;

    public Tuplet(T typeA, Q typeB) {
        this.typeA = typeA;
        this.typeB = typeB;
    }

    public T getTypeA() {
        return typeA;
    }

    public Q getTypeB() {
        return typeB;
    }


}
