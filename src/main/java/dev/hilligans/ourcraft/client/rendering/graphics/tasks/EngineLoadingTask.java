package dev.hilligans.ourcraft.client.rendering.graphics.tasks;

import dev.hilligans.ourcraft.GameInstance;
import dev.hilligans.ourcraft.client.Client;
import dev.hilligans.ourcraft.client.MatrixStack;
import dev.hilligans.ourcraft.client.rendering.Textures;
import dev.hilligans.ourcraft.client.rendering.graphics.PipelineState;
import dev.hilligans.ourcraft.client.rendering.graphics.RenderTask;
import dev.hilligans.ourcraft.client.rendering.graphics.RenderTaskSource;
import dev.hilligans.ourcraft.client.rendering.graphics.RenderWindow;
import dev.hilligans.ourcraft.client.rendering.graphics.api.GraphicsContext;
import dev.hilligans.ourcraft.client.rendering.graphics.api.IGraphicsEngine;
import dev.hilligans.ourcraft.client.rendering.screens.JoinScreen;
import dev.hilligans.ourcraft.client.rendering.world.StringRenderer;
import dev.hilligans.ourcraft.util.Settings;

public class EngineLoadingTask extends RenderTaskSource {

    public EngineLoadingTask() {
        super("engine_loading_task", "ourcraft:engine_loading_target");
    }

    @Override
    public RenderTask getDefaultTask() {
        return new RenderTask() {
            @Override
            public void draw(RenderWindow window, GraphicsContext graphicsContext, IGraphicsEngine<?, ?, ?> engine, Client client, MatrixStack worldStack, MatrixStack screenStack, float delta) {
                GameInstance gameInstance = engine.getGameInstance();
                if(gameInstance.loaderPipeline != null) {
                    try {
                        StringRenderer rend = engine.getStringRenderer();
                        int index = gameInstance.loaderPipeline.section.subsectionCount;
                        var stage = gameInstance.loaderPipeline.stages.get(index - 1);
                        screenStack.push();
                        screenStack.setColor(1.0f,1.0f,1.0f);
                        engine.getDefaultImpl().uploadMatrix(graphicsContext, screenStack, Textures.BACKFILL.shaderSource);

                        rend.drawCenteredStringInternal(window, graphicsContext, screenStack, stage.getTypeA(), 29, 0.5f);
                        rend.drawCenteredStringInternal(window, graphicsContext, screenStack, index+" of "+gameInstance.loaderPipeline.section.subsectionLength, 29 * 2, 0.5f);
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
