package dev.hilligans.ourcraft.client.input;

import dev.hilligans.ourcraft.client.rendering.graphics.api.IInputProvider;
import dev.hilligans.ourcraft.mod.handler.content.ModContent;
import dev.hilligans.ourcraft.util.registry.IRegistryElement;

public abstract class InputHandlerProvider implements IRegistryElement {

    public String name;
    public ModContent modContent;

    public InputHandlerProvider(String name) {
        this.name = name;
    }

    public abstract IInputProvider getProvider(String engineName, String windowingName);

    @Override
    public void assignModContent(ModContent modContent) {
        this.modContent = modContent;
    }

    @Override
    public String getResourceName() {
        return name;
    }

    @Override
    public String getIdentifierName() {
        return modContent.getModID() + ":" + name;
    }

    @Override
    public String getUniqueName() {
        return "input." + modContent.getModID() + "." + name;
    }
}
