package dev.hilligans.ourcraft.client.input;

import dev.hilligans.ourcraft.client.rendering.graphics.api.IInputProvider;
import dev.hilligans.ourcraft.mod.handler.content.ModContainer;
import dev.hilligans.ourcraft.mod.handler.content.ModContent;
import dev.hilligans.ourcraft.util.registry.IRegistryElement;

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
