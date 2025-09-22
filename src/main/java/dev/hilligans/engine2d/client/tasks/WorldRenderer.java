package dev.hilligans.engine2d.client.tasks;

import dev.hilligans.engine.GameInstance;
import dev.hilligans.engine.client.graphics.RenderTask;
import dev.hilligans.engine.client.graphics.RenderTaskSource;
import dev.hilligans.engine.client.graphics.RenderWindow;
import dev.hilligans.engine.client.graphics.ShaderSource;
import dev.hilligans.engine.client.graphics.api.GraphicsContext;
import dev.hilligans.engine.client.graphics.api.IDefaultEngineImpl;
import dev.hilligans.engine.client.graphics.api.IGraphicsEngine;
import dev.hilligans.engine.client.graphics.resource.MatrixStack;
import dev.hilligans.engine2d.client.Client2D;
import dev.hilligans.engine2d.client.sprite.ISpriteEntity;
import dev.hilligans.engine2d.client.sprite.Sprite;
import dev.hilligans.engine2d.world.SpriteEntity;
import dev.hilligans.engine2d.world.World2D;
import org.joml.Vector3d;
import org.joml.Vector3f;

import java.util.Random;

public class WorldRenderer extends RenderTaskSource {

    public WorldRenderer() {
        super("world_renderer_2d");
    }

    @Override
    public RenderTask<Client2D> getDefaultTask() {
        return new RenderTask<>() {

            ShaderSource shaderSource;

            @Override
            public void draw(RenderWindow window, GraphicsContext graphicsContext, IGraphicsEngine<?, ?, ?> engine, Client2D client, MatrixStack worldStack, MatrixStack screenStack, float delta) {
                IDefaultEngineImpl<?,?,?> impl = engine.getDefaultImpl();
                World2D world = client.getWorld();

                worldStack.translate(window.getCamera().getPosition());

                impl.uploadMatrix(graphicsContext, worldStack, shaderSource);
                impl.bindPipeline(graphicsContext, shaderSource.program);

                world.getScene().draw(engine, graphicsContext, worldStack);

                for(ISpriteEntity entity : world.getRenderableEntities()) {
                    Sprite sprite = entity.getSprite();
                    entity.tickVisuals(graphicsContext);
                    sprite.draw(entity, engine, graphicsContext, worldStack);
                }
            }

            @Override
            public void load(GameInstance gameInstance, IGraphicsEngine<?, ?, ?> graphicsEngine, GraphicsContext graphicsContext) {
                shaderSource = gameInstance.SHADERS.get("ourcraft:position_texture");
            }
        };
    }
}
