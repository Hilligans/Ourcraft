package dev.hilligans.ourcraft.client.rendering.screens.container.screens;

import dev.hilligans.ourcraft.client.Client;
import dev.hilligans.ourcraft.client.MatrixStack;
import dev.hilligans.ourcraft.client.rendering.ContainerScreen;
import dev.hilligans.ourcraft.client.rendering.graphics.RenderWindow;
import dev.hilligans.ourcraft.container.Container;

public abstract class CustomContainerScreen extends ContainerScreen<Container> {

    public CustomContainerScreen(Client client) {
        super(client);
    }

    @Override
    public abstract void drawScreen(RenderWindow window, MatrixStack matrixStack);

    @Override
    public Container getContainer() {
        return null;
    }


}
