package dev.Hilligans.ourcraft.Client.Input;

import dev.Hilligans.ourcraft.Client.Input.Key.KeyPress;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.RenderWindow;
import dev.Hilligans.ourcraft.ModHandler.Content.ModContent;
import dev.Hilligans.ourcraft.Util.Registry.IRegistryElement;

import java.nio.ByteBuffer;

public abstract class Input implements IRegistryElement {

    public String key;
    public String modID;

    public String boundKey;

    public ModContent modContent;

    public Input(String defaultBind) {
        bind(defaultBind);
    }

    public Input() {
    }

    abstract KeyPress getKeyPress(RenderWindow renderWindow);

    public void bind(String key) {
        this.boundKey = key;
    }

    @Override
    public String getResourceName() {
        return key;
    }

    @Override
    public String getIdentifierName() {
        return modID + ":" + key;
    }

    @Override
    public String getUniqueName() {
        return "key_bind." + modID + "." + key;
    }

    public void setModContent(ModContent modContent) {
        this.modContent = modContent;
        this.modID = modContent.getModID();
    }
}
