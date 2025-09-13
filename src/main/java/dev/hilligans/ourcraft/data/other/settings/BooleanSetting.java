package dev.hilligans.ourcraft.data.other.settings;

import dev.hilligans.engine.tag.CompoundNBTTag;

public class BooleanSetting implements Setting {

    String name;
    boolean value;

    public BooleanSetting(String name) {
        this(name,false);
    }

    public BooleanSetting(String name, boolean defaultValue) {
        this.name = name;
        this.value = defaultValue;
    }


    @Override
    public void read(CompoundNBTTag compoundTag) {
        value = compoundTag.getBoolean(name);
    }

    @Override
    public void write(CompoundNBTTag compoundTag) {
        compoundTag.putBoolean(name,value);
    }
}
