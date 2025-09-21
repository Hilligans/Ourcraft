package dev.hilligans.engine2d.client;

import dev.hilligans.engine.GameInstance;
import dev.hilligans.engine.application.IClientApplication;
import dev.hilligans.engine.client.graphics.RenderWindow;
import dev.hilligans.engine.client.graphics.Screen;
import dev.hilligans.engine.client.graphics.api.IGraphicsEngine;
import dev.hilligans.engine.util.ThreadContext;
import dev.hilligans.engine2d.client.sprite.ISpriteEntity;
import dev.hilligans.engine2d.client.sprite.Sprite;
import dev.hilligans.engine2d.world.SpriteEntity;
import dev.hilligans.engine2d.world.World2D;
import dev.hilligans.ourcraft.client.rendering.FreeCamera;
import dev.hilligans.ourcraft.entity.EntityType;

public class Client2D implements IClientApplication {

    public GameInstance gameInstance;

    public RenderWindow window;
    public Screen openScreen;

    public World2D world = new World2D();

    public World2D getWorld() {
        return world;
    }

    @Override
    public Screen getOpenScreen() {
        return openScreen;
    }

    @Override
    public void tick(ThreadContext threadContext) {

    }

    @Override
    public void openScreen(Screen screen) {
        this.openScreen = screen;
    }

    @Override
    public RenderWindow getRenderWindow() {
        return window;
    }

    @Override
    public GameInstance getGameInstance() {
        return gameInstance;
    }

    @Override
    public void postCoreStartApplication(GameInstance gameInstance) {
    }

    @Override
    public void startApplication(GameInstance gameInstance) {
        IGraphicsEngine<?, ?, ?> engine = gameInstance.get("ourcraft:openglEngine", IGraphicsEngine.class);

        Thread thread = new Thread(() -> {
            try {
                window = engine.startEngine();
                window.camera = new Camera2D(window, 1200, 800);

                window.setClient(this);

                window.setClearColor(0.2f, 0.3f, 0.3f, 1.0f);

                window.setRenderPipeline("engine2D:pipeline2d");
                engine.createRenderLoop(gameInstance, window).run();
                engine.close();

                gameInstance.THREAD_PROVIDER.EXECUTOR.shutdownNow();
                System.exit(0);

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        thread.start();

        world.addEntity(new SpriteEntity(
                gameInstance.getExcept("engine2D:entity_type", EntityType.class),
                gameInstance.getExcept("engine2D:test_sprite", Sprite.class)) {
        });
    }

    @Override
    public String getResourceName() {
        return "client2D";
    }

    @Override
    public String getResourceOwner() {
        return "engine2D";
    }
}
