package dev.Hilligans.Data.Other.Settings;

import dev.Hilligans.Tag.CompoundTag;

public interface Setting {

    void read(CompoundTag compoundTag);

    void write(CompoundTag compoundTag);


}
