package dev.hilligans.engine.client.input;

import dev.hilligans.engine.client.graphics.api.IInputProvider;
import dev.hilligans.engine.mod.handler.content.ModContainer;
import dev.hilligans.engine.util.registry.IRegistryElement;

public abstract class InputHandlerProvider implements IRegistryElement {

    public String name;
    public ModContainer owner;

    public InputHandlerProvider(String name) {
        this.name = name;
    }

    public abstract IInputProvider getProvider(String engineName, String windowingName);

    @Override
    public void assignOwner(ModContainer modContent) {
        this.owner = modContent;
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
        return "input";
    }
}
