package dev.hilligans.ourcraft.client.rendering.graphics.api;

public class GraphicsContext {

    public int getContextID() {
        return 0;
    }

    public boolean pipelineStateSet = false;

    public GraphicsContext setPipelineState(boolean state) {
        this.pipelineStateSet = state;
        return this;
    }

}
