package dev.hilligans.ourcraft.Client.Rendering.Graphics;

import dev.hilligans.ourcraft.Client.Client;
import dev.hilligans.ourcraft.Client.MatrixStack;
import dev.hilligans.ourcraft.Client.Rendering.Graphics.API.GraphicsContext;
import dev.hilligans.ourcraft.Client.Rendering.Graphics.API.IGraphicsEngine;

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
