package dev.hilligans.ourcraft.settings;

import dev.hilligans.engine.mod.handler.content.ModContainer;
import dev.hilligans.engine.util.Side;
import dev.hilligans.engine.util.registry.IRegistryElement;

public abstract class Setting implements IRegistryElement {

    public Side side = Side.COMMON;
    public String name;
    public ModContainer source;

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
    public void assignOwner(ModContainer source) {
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
