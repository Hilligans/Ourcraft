package dev.hilligans.ourcraft.util;

import dev.hilligans.engine.util.IByteArray;
import dev.hilligans.engine.tag.CompoundNBTTag;

public interface IPosition {

    long getRawX();
    long getRawY();
    long getRawZ();

    double getX();
    double getY();
    double getZ();

    void add(double x, double y, double z);
    void set(double x, double y, double z);

    IPosition add(IPosition b, IPosition dest);

    default IPosition add(IPosition a) {
        return add(a,this);
    }

    IPosition sub(IPosition b, IPosition dest);

    default IPosition sub(IPosition b) {
        return sub(b, this);
    }

    void write(CompoundNBTTag tag);
    void write(IByteArray byteArray);

    boolean isInteger();
}
