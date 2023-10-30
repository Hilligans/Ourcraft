package dev.hilligans.ourcraft.WorldSave;

import dev.hilligans.ourcraft.Tag.CompoundNBTTag;
import dev.hilligans.ourcraft.Util.ByteArray;

public interface IEncodeable {

    void write(CompoundNBTTag compoundNBTTag);
    void write(ByteArray byteArray);

    void read(CompoundNBTTag compoundNBTTag);
    void read(ByteArray byteArray);

}
