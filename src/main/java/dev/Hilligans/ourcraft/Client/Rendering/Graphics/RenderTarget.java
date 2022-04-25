package dev.Hilligans.ourcraft.Client.Rendering.Graphics;

import dev.Hilligans.ourcraft.Client.MatrixStack;
import dev.Hilligans.ourcraft.GameInstance;
import dev.Hilligans.ourcraft.ModHandler.Content.ModContent;
import dev.Hilligans.ourcraft.Util.Registry.IRegistryElement;

public class RenderTarget implements IRegistryElement {

    public String renderPipeline;
    public String name;
    public String after;
    public String before;
    public String targetedMod;
    public MatrixStack matrixStack;
    public ModContent modContent;

    public PipelineState pipelineState;

    public RenderTarget(String name, String renderPipeline) {
        this.name = name;
        this.renderPipeline = renderPipeline;
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

    public RenderTarget setPipelineState(PipelineState state) {
        this.pipelineState = state;
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
        return "render_target." + modContent.getModID() + "." + name;
    }
}
