package dev.hilligans.ourcraft.client.rendering.graphics.api;

import dev.hilligans.ourcraft.util.ThreadContext;
import dev.hilligans.ourcraft.util.sections.ISection;
import org.jetbrains.annotations.NotNull;

public class GraphicsContext extends ThreadContext {

    public int getContextID() {
        return 0;
    }

    public boolean pipelineStateSet = false;

    public GraphicsContext setPipelineState(boolean state) {
        this.pipelineStateSet = state;
        return this;
    }

    public GraphicsContext setSection(@NotNull ISection section) {
        super.setSection(section);
        return this;
    }
}
