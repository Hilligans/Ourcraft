package dev.hilligans.ourcraft.client.rendering.graphics;

import dev.hilligans.ourcraft.client.Client;
import dev.hilligans.ourcraft.client.MatrixStack;
import dev.hilligans.ourcraft.client.rendering.graphics.api.GraphicsContext;
import dev.hilligans.ourcraft.client.rendering.graphics.api.IGraphicsEngine;

public abstract class RenderTask {

    public abstract void draw(RenderWindow window, GraphicsContext graphicsContext, IGraphicsEngine<?, ?, ?> engine, Client client, MatrixStack worldStack, MatrixStack screenStack, float delta);


    /**
     * If this method returns not null that means the pipeline state must be fixed for the entire task and will throw an exception if the state is attempted to be modified
     */
    public PipelineState getPipelineState() {
        return null;
    }

    public void close() {
    }

}
