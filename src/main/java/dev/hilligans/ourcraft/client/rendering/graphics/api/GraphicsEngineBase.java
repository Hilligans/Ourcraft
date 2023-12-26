package dev.hilligans.ourcraft.client.rendering.graphics.api;

import dev.hilligans.ourcraft.client.rendering.graphics.RenderWindow;
import dev.hilligans.ourcraft.client.rendering.world.StringRenderer;
import dev.hilligans.ourcraft.GameInstance;

import java.util.ArrayList;

public abstract class GraphicsEngineBase<Q extends RenderWindow,V extends IDefaultEngineImpl<Q,X>, X extends GraphicsContext> implements IGraphicsEngine<Q,V,X> {

    public StringRenderer stringRenderer;
    public ArrayList<Q> windows = new ArrayList<>();

    public GameInstance gameInstance;


    public GraphicsEngineBase() {
    }

    @Override
    public StringRenderer getStringRenderer() {
        return stringRenderer;
    }

    @Override
    public void setupStringRenderer(String defaultLanguage) {
        stringRenderer = new StringRenderer(this);
        stringRenderer.buildChars();
    }

    @Override
    public ArrayList<Q> getWindows() {
        return windows;
    }

    @Override
    public void load(GameInstance gameInstance) {
        this.gameInstance = gameInstance;
    }

    @Override
    public GameInstance getGameInstance() {
        return gameInstance;
    }
}
