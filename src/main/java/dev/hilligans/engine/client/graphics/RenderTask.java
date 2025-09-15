package dev.hilligans.engine.client.graphics;

import dev.hilligans.engine.GameInstance;
import dev.hilligans.engine.application.IClientApplication;
import dev.hilligans.engine.client.graphics.resource.MatrixStack;
import dev.hilligans.engine.client.graphics.api.GraphicsContext;
import dev.hilligans.engine.client.graphics.api.IGraphicsElement;
import dev.hilligans.engine.client.graphics.api.IGraphicsEngine;

public abstract class RenderTask<T extends IClientApplication> implements IGraphicsElement {

    protected String identifierName;

    public abstract void draw(RenderWindow window, GraphicsContext graphicsContext, IGraphicsEngine<?, ?, ?> engine, T client, MatrixStack worldStack, MatrixStack screenStack, float delta);

    public void ndraw(RenderWindow window, GraphicsContext graphicsContext, IGraphicsEngine<?, ?, ?> engine, IClientApplication client, MatrixStack worldStack, MatrixStack screenStack, float delta) {
        this.draw(window, graphicsContext, engine, (T)client, worldStack, screenStack, delta);
    }

    /**
     * If this method returns not null that means the pipeline state must be fixed for the entire task and will throw an exception if the state is attempted to be modified
     */
    public PipelineState getPipelineState() {
        return null;
    }

    public void close() {
    }

    public RenderTask<T> setNameIdentifierName(String name) {
        this.identifierName = name;
        return this;
    }

    public String getIdentifierName() {
        return identifierName;
    }

    @Override
    public void load(GameInstance gameInstance, IGraphicsEngine<?, ?, ?> graphicsEngine, GraphicsContext graphicsContext) {}

    @Override
    public void cleanup(GameInstance gameInstance, IGraphicsEngine<?, ?, ?> graphicsEngine, GraphicsContext graphicsContext) {}
}
