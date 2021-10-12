package dev.Hilligans.ourcraft.Data.Other.Settings;

import dev.Hilligans.ourcraft.Tag.CompoundTag;

public interface Setting {

    void read(CompoundTag compoundTag);

    void write(CompoundTag compoundTag);


}
