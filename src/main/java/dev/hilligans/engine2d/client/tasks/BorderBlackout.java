package dev.hilligans.engine2d.client.tasks;

import dev.hilligans.engine.GameInstance;
import dev.hilligans.engine.application.IClientApplication;
import dev.hilligans.engine.client.graphics.RenderTask;
import dev.hilligans.engine.client.graphics.RenderTaskSource;
import dev.hilligans.engine.client.graphics.RenderWindow;
import dev.hilligans.engine.client.graphics.ShaderSource;
import dev.hilligans.engine.client.graphics.api.GraphicsContext;
import dev.hilligans.engine.client.graphics.api.IDefaultEngineImpl;
import dev.hilligans.engine.client.graphics.api.IGraphicsEngine;
import dev.hilligans.engine.client.graphics.api.IMeshBuilder;
import dev.hilligans.engine.client.graphics.resource.MatrixStack;
import dev.hilligans.engine.client.graphics.resource.VertexFormat;
import dev.hilligans.engine2d.client.Camera2D;

public class BorderBlackout extends RenderTaskSource {
    public BorderBlackout() {
        super("border_blackout");
    }

    @Override
    public RenderTask<?> getDefaultTask() {
        return new RenderTask<>() {
            VertexFormat vertexFormat;
            ShaderSource program;

            @Override
            public void draw(RenderWindow window, GraphicsContext graphicsContext, IGraphicsEngine<?, ?, ?> engine, IClientApplication client, MatrixStack worldStack, MatrixStack screenStack, float delta) {
                if(window.getCamera() instanceof Camera2D camera) {
                    IDefaultEngineImpl<?,?,?> impl = engine.getDefaultImpl();

                    IMeshBuilder builder = engine.getDefaultImpl().getMeshBuilder(vertexFormat);

                    int distanceX = (int) camera.getInsetX();
                    int distanceY = (int) camera.getInsetY();
                    int endX = (int) (distanceX + camera.worldWidth * camera.getScale());
                    int endY = (int) (distanceY + camera.worldHeight * camera.getScale());

                    addQuad(builder, 0, 0, distanceX, camera.getWindowHeight());
                    addQuad(builder, endX, 0, camera.getWindowWidth(), camera.getWindowHeight());
                    addQuad(builder, distanceX, 0, endX, distanceY);
                    addQuad(builder, distanceX, endY, endX, camera.getWindowHeight());


                    impl.bindPipeline(graphicsContext, program.program);
                    impl.uploadMatrix(graphicsContext, screenStack, program);
                    impl.drawAndDestroyMesh(graphicsContext, screenStack, builder);
                }
            }

            @Override
            public void load(GameInstance gameInstance, IGraphicsEngine<?, ?, ?> graphicsEngine, GraphicsContext graphicsContext) {
                program = gameInstance.getExcept("ourcraft:position_color_shader", ShaderSource.class);
                vertexFormat = gameInstance.getExcept("ourcraft:position_color", VertexFormat.class);
            }
        };
    }

    public static void addQuad(IMeshBuilder builder, float x, float y, float maxX, float maxY) {
        int s = builder.getVertexCount();

        float R = 0;
        float G = 0;
        float B = 0;
        float A = 1;

        builder.addVertices(x,    y,    1, R, G, B, A,
                maxX, y,    1, R, G, B, A,
                x,    maxY, 1, R, G, B, A,
                maxX, maxY, 1, R, G, B, A);

        builder.addCounterClockwiseIndices(s);
    }
}
