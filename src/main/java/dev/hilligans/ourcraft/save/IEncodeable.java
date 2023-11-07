package dev.hilligans.ourcraft.save;

import dev.hilligans.ourcraft.tag.CompoundNBTTag;
import dev.hilligans.ourcraft.util.IByteArray;

public interface IEncodeable {

    void write(CompoundNBTTag compoundNBTTag);
    void write(IByteArray byteArray);

    void read(CompoundNBTTag compoundNBTTag);
    void read(IByteArray byteArray);

}
