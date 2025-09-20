package dev.hilligans.engine.client.graphics.api;

import dev.hilligans.engine.GameInstance;
import dev.hilligans.engine.client.graphics.RenderWindow;
import dev.hilligans.engine.client.graphics.util.StringRenderer;

import java.util.ArrayList;

public abstract class GraphicsEngineBase<Q extends RenderWindow,V extends IDefaultEngineImpl<Q,X,?>, X extends GraphicsContext> implements IGraphicsEngine<Q,V,X> {

    public StringRenderer stringRenderer;
    public ArrayList<Q> windows = new ArrayList<>();

    public GameInstance gameInstance;
    public boolean running = false;


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
    public boolean isRunning() {
        return running;
    }

    @Override
    public void preLoad(GameInstance gameInstance) {
        this.gameInstance = gameInstance;
    }

    @Override
    public GameInstance getGameInstance() {
        return gameInstance;
    }
}
