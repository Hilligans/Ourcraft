package dev.hilligans.ourcraft.data.other.settings;

import dev.hilligans.ourcraft.tag.CompoundNBTTag;

public interface Setting {

    void read(CompoundNBTTag compoundTag);

    void write(CompoundNBTTag compoundTag);


}
