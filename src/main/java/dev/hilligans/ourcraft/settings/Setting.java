package dev.hilligans.ourcraft.settings;

import dev.hilligans.ourcraft.mod.handler.content.ModContent;
import dev.hilligans.ourcraft.util.Side;
import dev.hilligans.ourcraft.util.registry.IRegistryElement;

public abstract class Setting implements IRegistryElement {

    public Side side = Side.COMMON;
    public String name;
    public ModContent source;

    public Setting(String name) {
        this.name = name;
    }

    public Setting(String name, Side side) {
        this(name);
        this.side = side;
    }

    abstract void read(String string);

    abstract String write();

    @Override
    public void assignModContent(ModContent source) {
        this.source = source;
    }

    @Override
    public String getResourceName() {
        return name;
    }

    @Override
    public String getResourceOwner() {
        return source.getModID();
    }

    @Override
    public String getResourceType() {
        return "setting";
    }
}
