package dev.hilligans.ourcraft.Data.Other.Settings;

import dev.hilligans.ourcraft.Tag.CompoundNBTTag;

public interface Setting {

    void read(CompoundNBTTag compoundTag);

    void write(CompoundNBTTag compoundTag);


}
