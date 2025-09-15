package dev.hilligans.engine.client.graphics;

import dev.hilligans.engine.client.graphics.resource.MatrixStack;
import dev.hilligans.engine.mod.handler.content.ModContainer;
import dev.hilligans.engine.util.registry.IRegistryElement;

public class RenderTarget implements IRegistryElement {

    public String renderPipeline;
    public String renderTask;
    public String name;
    public String after;
    public String before;
    public String targetedMod;
    public MatrixStack matrixStack;
    public ModContainer owner;

    public PipelineState pipelineState;

    public RenderTarget(String name, String renderPipeline, String renderTask) {
        this.name = name;
        this.renderPipeline = renderPipeline;
        this.renderTask = renderTask;
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

    public String getRenderTask() {
        return renderTask;
    }

    public void resetFrame() {
        matrixStack = null;
    }

    @Override
    public void assignOwner(ModContainer owner) {
        this.owner = owner;
    }

    @Override
    public String getResourceName() {
        String before = this.before == null ? "" : this.before;
        String after = this.after == null ? "" : this.after;

        return before + "-" + name + "-" + renderPipeline.replace(":", "_") + "-" + renderTask.replace(":", "_") + "-" + after;
    }

    @Override
    public String getResourceOwner() {
        return owner.getModID();
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
