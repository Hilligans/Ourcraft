package dev.hilligans.engine.util;

import dev.hilligans.engine.tag.CompoundNBTTag;

public interface IEncodeable {

    void write(CompoundNBTTag compoundNBTTag);
    void write(IByteArray byteArray);

    void read(CompoundNBTTag compoundNBTTag);
    void read(IByteArray byteArray);

}
