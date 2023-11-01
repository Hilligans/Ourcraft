package dev.hilligans.ourcraft.WorldSave;

import dev.hilligans.ourcraft.Tag.CompoundNBTTag;
import dev.hilligans.ourcraft.Util.ByteArray;
import dev.hilligans.ourcraft.Util.IByteArray;

public interface IEncodeable {

    void write(CompoundNBTTag compoundNBTTag);
    void write(IByteArray byteArray);

    void read(CompoundNBTTag compoundNBTTag);
    void read(IByteArray byteArray);

}
