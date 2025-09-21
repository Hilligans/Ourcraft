package dev.hilligans.engine2d.client;

import dev.hilligans.engine.application.IClientApplication;
import dev.hilligans.engine.client.graphics.RenderTask;
import dev.hilligans.engine.client.graphics.RenderTaskSource;
import dev.hilligans.engine.client.graphics.RenderWindow;
import dev.hilligans.engine.client.graphics.ShaderSource;
import dev.hilligans.engine.client.graphics.api.GraphicsContext;
import dev.hilligans.engine.client.graphics.api.IDefaultEngineImpl;
import dev.hilligans.engine.client.graphics.api.IGraphicsEngine;
import dev.hilligans.engine.client.graphics.resource.MatrixStack;
import dev.hilligans.engine2d.client.sprite.ISpriteEntity;
import dev.hilligans.engine2d.client.sprite.Sprite;
import dev.hilligans.engine2d.world.SpriteEntity;
import dev.hilligans.engine2d.world.World2D;
import dev.hilligans.ourcraft.entity.IEntity;

import java.util.Random;

public class WorldRenderer extends RenderTaskSource {

    public WorldRenderer() {
        super("world_renderer_2d");
    }

    @Override
    public RenderTask<Client2D> getDefaultTask() {
        return new RenderTask<>() {
            @Override
            public void draw(RenderWindow window, GraphicsContext graphicsContext, IGraphicsEngine<?, ?, ?> engine, Client2D client, MatrixStack worldStack, MatrixStack screenStack, float delta) {
                IDefaultEngineImpl<?,?,?> impl = engine.getDefaultImpl();
                World2D world = client.getWorld();

                ShaderSource shaderSource = engine.getGameInstance().SHADERS.get("ourcraft:position_texture");

                impl.uploadMatrix(graphicsContext, worldStack, shaderSource);
                impl.bindPipeline(graphicsContext, shaderSource.program);

                Random random = new Random();
                for(ISpriteEntity entity : world.getRenderableEntities()) {
                    Sprite sprite = entity.getSprite();
                    if(entity instanceof SpriteEntity e) {
                        if(random.nextInt(100) == 0) {
                            e.spriteIndex = (e.spriteIndex + 1) % 4;
                        }
                    }
                    sprite.draw(entity, engine, graphicsContext, worldStack);
                }
            }
        };
    }
}
