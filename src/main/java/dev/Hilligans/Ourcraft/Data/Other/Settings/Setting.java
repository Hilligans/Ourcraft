package dev.Hilligans.Ourcraft.Data.Other.Settings;

import dev.Hilligans.Ourcraft.Tag.CompoundTag;

public interface Setting {

    void read(CompoundTag compoundTag);

    void write(CompoundTag compoundTag);


}
