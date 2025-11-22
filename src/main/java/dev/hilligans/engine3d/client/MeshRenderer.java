package dev.hilligans.engine3d.client;

import dev.hilligans.engine.GameInstance;
import dev.hilligans.engine.application.IClientApplication;
import dev.hilligans.engine.client.graphics.RenderTask;
import dev.hilligans.engine.client.graphics.RenderTaskSource;
import dev.hilligans.engine.client.graphics.RenderWindow;
import dev.hilligans.engine.client.graphics.ShaderSource;
import dev.hilligans.engine.client.graphics.api.GraphicsContext;
import dev.hilligans.engine.client.graphics.api.IGraphicsEngine;
import dev.hilligans.engine.client.graphics.api.IMeshBuilder;
import dev.hilligans.engine.client.graphics.api.IModel;
import dev.hilligans.engine.client.graphics.resource.MatrixStack;
import dev.hilligans.engine.resource.ResourceLocation;

public class MeshRenderer extends RenderTaskSource {
    public MeshRenderer() {
        super("mesh_render_task");
    }

    public ShaderSource shaderSource;

    @Override
    public RenderTask<?> getDefaultTask() {
        return new RenderTask<>() {
            @Override
            public void draw(RenderWindow window, GraphicsContext graphicsContext, IGraphicsEngine<?, ?, ?> engine, IClientApplication client, MatrixStack worldStack, MatrixStack screenStack, float delta) {
                if (mesh != 0) {
                    engine.getDefaultImpl().bindPipeline(graphicsContext, shaderSource.program);
                    engine.getDefaultImpl().uploadMatrix(graphicsContext, worldStack, shaderSource);
                    engine.getDefaultImpl().drawMesh(graphicsContext, worldStack, mesh, 0, (int)length);
                }
            }

            long mesh = 0;
            long length;

            @Override
            public void load(GameInstance gameInstance, IGraphicsEngine<?, ?, ?> graphicsEngine, GraphicsContext graphicsContext) {
                IModel model = gameInstance.RESOURCE_LOADER.getResource(new ResourceLocation("Data/barrel.obj", "ourcraft"), IModel.class);
                IMeshBuilder builder = model.build(graphicsEngine.getDefaultImpl());
                this.length = builder.getIndexCount();


                this.mesh = graphicsEngine.getDefaultImpl().createMesh(graphicsContext, builder);
            }
        };
    }

    @Override
    public void preLoad(GameInstance gameInstance) {
        super.preLoad(gameInstance);
        shaderSource = gameInstance.SHADERS.get("ourcraft:position_color_shader");
    }
}
