package dev.hilligans.engine.client.graphics.tasks;

import dev.hilligans.engine.application.IClientApplication;
import dev.hilligans.engine.client.graphics.RenderTask;
import dev.hilligans.engine.client.graphics.RenderTaskSource;
import dev.hilligans.engine.client.graphics.RenderWindow;
import dev.hilligans.engine.client.graphics.api.GraphicsContext;
import dev.hilligans.engine.client.graphics.api.IGraphicsEngine;
import dev.hilligans.engine.client.graphics.resource.MatrixStack;

public class BlankRenderTask extends RenderTaskSource {
    public BlankRenderTask() {
        super("blank_render_task");
    }

    @Override
    public RenderTask<IClientApplication> getDefaultTask() {
        return new RenderTask<>() {
            @Override
            public void draw(RenderWindow window, GraphicsContext graphicsContext, IGraphicsEngine<?, ?, ?> engine, IClientApplication client, MatrixStack worldStack, MatrixStack screenStack, float delta) {}
        };
    }
}
