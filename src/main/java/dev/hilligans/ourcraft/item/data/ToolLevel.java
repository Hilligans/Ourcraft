package dev.hilligans.ourcraft.item.data;

import dev.hilligans.ourcraft.mod.handler.Identifier;
import dev.hilligans.ourcraft.mod.handler.content.ModContainer;
import dev.hilligans.ourcraft.mod.handler.content.ModContent;
import dev.hilligans.ourcraft.util.registry.IRegistryElement;

public class ToolLevel implements IRegistryElement {

    public Identifier name;
    public ModContainer source;

    public ToolLevel(Identifier name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ToolLevel{" +
                "name=" + name +
                '}';
    }

    @Override
    public void assignOwner(ModContainer source) {
        this.source = source;
    }

    @Override
    public String getResourceName() {
        return name.getName();
    }

    @Override
    public String getResourceOwner() {
        return source.getModID();
    }

    @Override
    public String getResourceType() {
        return "tool_level";
    }
}
