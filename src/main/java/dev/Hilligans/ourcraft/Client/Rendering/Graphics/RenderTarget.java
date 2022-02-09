package dev.Hilligans.ourcraft.Client.Rendering.Graphics;

import dev.Hilligans.ourcraft.Client.MatrixStack;
import dev.Hilligans.ourcraft.GameInstance;
import dev.Hilligans.ourcraft.ModHandler.Content.ModContent;
import dev.Hilligans.ourcraft.Util.Registry.IRegistryElement;

public class RenderTarget implements IRegistryElement {

    public String name;
    public String after;
    public String before;
    public String targetedMod;
    public MatrixStack matrixStack;
    public ModContent modContent;

    public RenderTarget(String name) {
        this.name = name;
    }

    public RenderTarget afterTarget(String after, String modID) {
        this.after = after;
        this.targetedMod = modID;
        return this;
    }

    public RenderTarget beforeTarget(String before, String modID) {
        this.before = before;
        this.targetedMod = modID;
        return this;
    }

    public void resetFrame() {
        matrixStack = null;
    }

    @Override
    public void assignModContent(ModContent modContent) {
        this.modContent = modContent;
    }

    @Override
    public String getResourceName() {
        return name;
    }

    @Override
    public String getUniqueName() {
        return "renderTarget." + modContent.modID + "." + name;
    }
}
