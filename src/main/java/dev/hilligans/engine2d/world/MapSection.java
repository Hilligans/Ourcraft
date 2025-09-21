package dev.hilligans.engine2d.world;

import dev.hilligans.engine.mod.handler.content.ModContainer;
import dev.hilligans.engine.util.registry.IRegistryElement;

public class MapSection implements IRegistryElement {

    public ModContainer owner;
    public String name;


    public MapSection(String name) {
        this.name = name;
    }

    @Override
    public String getResourceName() {
        return name;
    }

    @Override
    public String getResourceOwner() {
        return owner.getModID();
    }

    @Override
    public String getResourceType() {
        return "map_section";
    }

    @Override
    public void assignOwner(ModContainer owner) {
        this.owner = owner;
    }
}
