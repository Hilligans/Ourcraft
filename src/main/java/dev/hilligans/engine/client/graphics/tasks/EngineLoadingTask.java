package dev.hilligans.engine.client.graphics.tasks;

import dev.hilligans.engine.GameInstance;
import dev.hilligans.engine.application.IClientApplication;
import dev.hilligans.engine.client.graphics.resource.MatrixStack;
import dev.hilligans.engine.client.graphics.PipelineState;
import dev.hilligans.engine.client.graphics.RenderTask;
import dev.hilligans.engine.client.graphics.RenderTaskSource;
import dev.hilligans.engine.client.graphics.RenderWindow;
import dev.hilligans.engine.client.graphics.api.GraphicsContext;
import dev.hilligans.engine.client.graphics.api.IGraphicsEngine;
import dev.hilligans.engine.client.graphics.util.StringRenderer;

public class EngineLoadingTask extends RenderTaskSource {

    public EngineLoadingTask() {
        super("engine_loading_task");
    }

    @Override
    public RenderTask<IClientApplication> getDefaultTask() {
        return new RenderTask<>() {
            @Override
            public void draw(RenderWindow window, GraphicsContext graphicsContext, IGraphicsEngine<?, ?, ?> engine, IClientApplication client, MatrixStack worldStack, MatrixStack screenStack, float delta) {
                GameInstance gameInstance = engine.getGameInstance();
                if (gameInstance.loaderPipeline != null) {
                    try {
                        StringRenderer rend = engine.getStringRenderer();
                        int index = gameInstance.loaderPipeline.section.subsectionCount;
                        var stage = gameInstance.loaderPipeline.stages.get(index - 1);
                        screenStack.push();
                        screenStack.setColor(1.0f, 1.0f, 1.0f);
                        //engine.getDefaultImpl().uploadMatrix(graphicsContext, screenStack, Textures.BACKFILL.data.get(gameInstance).shaderSource());

                        rend.drawCenteredStringInternal(window, graphicsContext, screenStack, stage.getTypeA(), 29, 0.5f);
                        rend.drawCenteredStringInternal(window, graphicsContext, screenStack, index + " of " + gameInstance.loaderPipeline.section.subsectionLength, 29 * 2, 0.5f);
                        screenStack.pop();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public PipelineState getPipelineState() {
                return new PipelineState().setDepth(false);
            }
        };

    }
}
