package dev.hilligans.ourcraft.client.rendering.graphics;

import dev.hilligans.ourcraft.client.MatrixStack;
import dev.hilligans.ourcraft.mod.handler.content.ModContent;
import dev.hilligans.ourcraft.util.registry.IRegistryElement;

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
    public String getResourceOwner() {
        return modContent.getModID();
    }

    @Override
    public String getResourceType() {
        return "render_target";
    }

    @Override
    public String toString() {
        return "RenderTarget{" +
                "renderPipeline='" + renderPipeline + '\'' +
                ", name='" + name + '\'' +
                ", after='" + after + '\'' +
                ", before='" + before + '\'' +
                ", targetedMod='" + targetedMod + '\'' +
                '}';
    }
}
