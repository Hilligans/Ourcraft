package dev.hilligans.ourcraft.Item.Data;

import dev.hilligans.ourcraft.ModHandler.Identifier;

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
