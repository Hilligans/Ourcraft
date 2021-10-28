package dev.Hilligans.ourcraft.Data.Other.Settings;

import dev.Hilligans.ourcraft.Tag.CompoundNBTTag;

public interface Setting {

    void read(CompoundNBTTag compoundTag);

    void write(CompoundNBTTag compoundTag);


}
