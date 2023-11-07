package dev.hilligans.ourcraft.item.data;

import dev.hilligans.ourcraft.mod.handler.Identifier;

public class ToolLevel {

    public Identifier name;

    public ToolLevel(Identifier name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ToolLevel{" +
                "name=" + name +
                '}';
    }
}
