package dev.hilligans.engine.client.graphics;

import dev.hilligans.engine.GameInstance;
import dev.hilligans.engine.client.graphics.api.GraphicsContext;
import dev.hilligans.engine.client.graphics.api.IGraphicsElement;
import dev.hilligans.engine.client.graphics.api.IGraphicsEngine;

import java.util.ArrayList;

public class PipelineInstance implements IGraphicsElement {


    public RenderPipeline renderPipeline;
    public ArrayList<RenderTask<?>> tasks;

    public PipelineInstance(RenderPipeline renderPipeline, ArrayList<RenderTask<?>> renderTasks) {
        this.renderPipeline = renderPipeline;
        this.tasks = renderTasks;
    }

    @Override
    public void load(GameInstance gameInstance, IGraphicsEngine<?, ?, ?> graphicsEngine, GraphicsContext graphicsContext) {
        for(RenderTask<?> renderTask : tasks) {
            renderTask.load(gameInstance, graphicsEngine, graphicsContext);
        }
    }

    @Override
    public void cleanup(GameInstance gameInstance, IGraphicsEngine<?, ?, ?> graphicsEngine, GraphicsContext graphicsContext) {
        for(RenderTask<?> renderTask : tasks) {
            renderTask.cleanup(gameInstance, graphicsEngine, graphicsContext);
        }
    }
}
