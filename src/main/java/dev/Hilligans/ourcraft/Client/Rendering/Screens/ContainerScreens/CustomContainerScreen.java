package dev.Hilligans.ourcraft.Client.Rendering.Screens.ContainerScreens;

import dev.Hilligans.ourcraft.Client.Client;
import dev.Hilligans.ourcraft.Client.MatrixStack;
import dev.Hilligans.ourcraft.Client.Rendering.ContainerScreen;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.RenderWindow;
import dev.Hilligans.ourcraft.Container.Container;

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
