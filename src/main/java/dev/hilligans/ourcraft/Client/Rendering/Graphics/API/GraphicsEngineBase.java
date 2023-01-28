package dev.hilligans.ourcraft.Client.Rendering.Graphics.API;

import dev.hilligans.ourcraft.Client.Rendering.Graphics.GraphicsData;
import dev.hilligans.ourcraft.Client.Rendering.Graphics.RenderWindow;
import dev.hilligans.ourcraft.Client.Rendering.World.StringRenderer;
import dev.hilligans.ourcraft.GameInstance;

import java.util.ArrayList;

public abstract class GraphicsEngineBase<Q extends RenderWindow,V extends IDefaultEngineImpl<Q,X>, X extends GraphicsContext> implements IGraphicsEngine<Q,V,X> {

    public StringRenderer stringRenderer;
    public ArrayList<Q> windows = new ArrayList<>();
    public GraphicsData graphicsData;

    public GameInstance gameInstance;


    public GraphicsEngineBase() {
        graphicsData = new GraphicsData(this);
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
    public GraphicsData getGraphicsData() {
        return graphicsData;
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
