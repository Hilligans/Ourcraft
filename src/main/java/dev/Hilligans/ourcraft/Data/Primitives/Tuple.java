package dev.Hilligans.ourcraft.Data.Primitives;

public class Tuple<T,Q> {

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

}
