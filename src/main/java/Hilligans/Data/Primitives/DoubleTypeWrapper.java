package Hilligans.Data.Primitives;

public class DoubleTypeWrapper<T,Q> {

    T typeA;
    Q typeB;

    public DoubleTypeWrapper(T typeA, Q typeB) {
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
