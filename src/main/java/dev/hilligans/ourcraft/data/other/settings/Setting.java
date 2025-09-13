package dev.hilligans.ourcraft.data.other.settings;

import dev.hilligans.engine.tag.CompoundNBTTag;

public interface Setting {

    void read(CompoundNBTTag compoundTag);

    void write(CompoundNBTTag compoundTag);


}
