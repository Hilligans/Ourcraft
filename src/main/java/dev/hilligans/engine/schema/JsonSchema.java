package dev.hilligans.engine.schema;

import dev.hilligans.engine.mod.handler.content.ModContainer;

public class JsonSchema implements Schema {

    public ModContainer owner;


    @Override
    public void assignOwner(ModContainer owner) {
        this.owner = owner;
    }

    @Override
    public String getResourceName() {
        return "";
    }

    @Override
    public String getResourceOwner() {
        return owner.getModID();
    }
}
