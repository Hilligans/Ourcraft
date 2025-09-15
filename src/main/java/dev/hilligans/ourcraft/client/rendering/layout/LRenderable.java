package dev.hilligans.ourcraft.client.rendering.layout;

import dev.hilligans.ourcraft.client.Client;
import dev.hilligans.engine.client.graphics.resource.MatrixStack;
import dev.hilligans.engine.client.graphics.RenderWindow;
import dev.hilligans.engine.client.graphics.api.GraphicsContext;
import dev.hilligans.engine.client.graphics.api.IGraphicsEngine;
import dev.hilligans.engine.client.graphics.api.ILayoutEngine;

public abstract non-sealed class LRenderable extends LWidget {

    public abstract void render(RenderWindow window, GraphicsContext graphicsContext, IGraphicsEngine<?, ?, ?> engine, Client client, MatrixStack matrixStack, ILayoutEngine<?> layoutEngine);

}
