package dev.hilligans.ourcraft.client.rendering.graphics;

public class PipelineState {

    public boolean depthTest = false;

    public PipelineState() {

    }

    public PipelineState setDepth(boolean val) {
        this.depthTest = val;
        return this;
    }

}
