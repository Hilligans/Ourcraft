package dev.hilligans.ourcraft.block.blockstate;

public interface IBlockStateType<T> {

    int getCount();

    T getDefaultValue();

    T getValue(int index);

    default IBlockStateType<T> setDefaultValue(T val) {
        return this;
    }

    int getIndexCast(T value);
    
    default int getIndex(Object obj) {
        return getIndex((T)obj);
    }
}
