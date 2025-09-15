package dev.hilligans.ourcraft.client.rendering.screens.container.screens;

import dev.hilligans.engine.client.graphics.resource.MatrixStack;
import dev.hilligans.ourcraft.client.rendering.ContainerScreen;
import dev.hilligans.engine.client.graphics.RenderWindow;
import dev.hilligans.engine.client.graphics.api.GraphicsContext;
import dev.hilligans.ourcraft.container.Container;

public abstract class CustomContainerScreen extends ContainerScreen<Container> {

    @Override
    public abstract void drawScreen(RenderWindow window, MatrixStack matrixStack, GraphicsContext graphicsContext);

    @Override
    public Container getContainer() {
        return null;
    }


}
