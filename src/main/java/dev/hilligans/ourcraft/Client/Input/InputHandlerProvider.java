package dev.hilligans.ourcraft.Client.Input;

import dev.hilligans.ourcraft.Client.Rendering.Graphics.API.IInputProvider;
import dev.hilligans.ourcraft.ModHandler.Content.ModContent;
import dev.hilligans.ourcraft.Util.Registry.IRegistryElement;

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
