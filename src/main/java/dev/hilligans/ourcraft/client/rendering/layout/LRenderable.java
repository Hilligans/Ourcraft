package dev.hilligans.ourcraft.client.rendering.layout;

import dev.hilligans.ourcraft.client.Client;
import dev.hilligans.ourcraft.client.MatrixStack;
import dev.hilligans.ourcraft.client.rendering.graphics.RenderWindow;
import dev.hilligans.ourcraft.client.rendering.graphics.api.GraphicsContext;
import dev.hilligans.ourcraft.client.rendering.graphics.api.IGraphicsEngine;
import dev.hilligans.ourcraft.client.rendering.graphics.api.ILayoutEngine;
import org.json.JSONObject;

public abstract non-sealed class LRenderable extends LWidget {

    public abstract void render(RenderWindow window, GraphicsContext graphicsContext, IGraphicsEngine<?, ?, ?> engine, Client client, MatrixStack matrixStack, ILayoutEngine<?> layoutEngine);

}
