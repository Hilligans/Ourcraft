package dev.Hilligans.ourcraft.Data.Other.Settings;

import dev.Hilligans.ourcraft.Tag.CompoundTag;

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
    public void read(CompoundTag compoundTag) {
        value = compoundTag.getBoolean(name);
    }

    @Override
    public void write(CompoundTag compoundTag) {
        compoundTag.putBoolean(name,value);
    }
}
