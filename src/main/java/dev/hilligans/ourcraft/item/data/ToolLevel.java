package dev.hilligans.ourcraft.item.data;

import dev.hilligans.ourcraft.mod.handler.Identifier;
import dev.hilligans.ourcraft.mod.handler.content.ModContent;
import dev.hilligans.ourcraft.util.registry.IRegistryElement;

public class ToolLevel implements IRegistryElement {

    public Identifier name;
    public ModContent source;

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
    public void assignModContent(ModContent modContent) {
        this.source = modContent;
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
