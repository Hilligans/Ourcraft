package dev.hilligans.ourcraft.entity;

import dev.hilligans.engine.util.registry.IRegistryElement;

public class EntityType implements IRegistryElement {

    public String owner;
    public String name;

    public int inventorySize;

    public EntityType(String name, String modID) {
        this.name = name;
        this.owner = modID;
    }

    public int getInventorySize() {
        return inventorySize;
    }

    @Override
    public String getResourceName() {
        return name;
    }

    @Override
    public String getResourceOwner() {
        return owner;
    }

    @Override
    public String getResourceType() {
        return "entity_type";
    }
}
