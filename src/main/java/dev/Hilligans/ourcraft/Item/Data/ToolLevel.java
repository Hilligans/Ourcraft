package dev.Hilligans.ourcraft.Item.Data;

import dev.Hilligans.ourcraft.ModHandler.Identifier;

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
