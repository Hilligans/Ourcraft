package dev.Hilligans.ourcraft.Client.Rendering.Graphics;

public class PipelineState {

    public boolean depthTest = false;

    public PipelineState() {

    }

    public PipelineState setDepth(boolean val) {
        this.depthTest = val;
        return this;
    }

}
