package dev.hilligans.ourcraft.client.rendering.graphics.layout;

import dev.hilligans.ourcraft.GameInstance;
import dev.hilligans.ourcraft.client.rendering.graphics.api.GraphicsContext;
import dev.hilligans.ourcraft.client.rendering.graphics.api.IGraphicsEngine;
import dev.hilligans.ourcraft.client.rendering.graphics.api.ILayoutEngine;
import dev.hilligans.ourcraft.client.rendering.layout.LWidget;
import org.json.JSONObject;

public class SimpleLayoutEngine implements ILayoutEngine<SimpleLayout> {
    @Override
    public SimpleLayout parseLayout(String layout) {
        return null;
    }

    public LWidget parseWidget(JSONObject layout) {


        return null;
    }

    @Override
    public void load(GameInstance gameInstance, IGraphicsEngine<?, ?, ?> graphicsEngine, GraphicsContext graphicsContext) {

    }

    @Override
    public void cleanup(GameInstance gameInstance, IGraphicsEngine<?, ?, ?> graphicsEngine, GraphicsContext graphicsContext) {

    }

    @Override
    public String getResourceName() {
        return "";
    }

    @Override
    public String getResourceOwner() {
        return "";
    }
}
