package dev.hilligans.engine.client.graphics.api;

import dev.hilligans.engine.util.ThreadContext;
import dev.hilligans.engine.util.sections.ISection;
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
