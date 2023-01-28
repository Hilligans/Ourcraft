package dev.hilligans.ourcraft.Client.Rendering.Screens.ContainerScreens;

import dev.hilligans.ourcraft.Client.Client;
import dev.hilligans.ourcraft.Client.MatrixStack;
import dev.hilligans.ourcraft.Client.Rendering.ContainerScreen;
import dev.hilligans.ourcraft.Client.Rendering.Graphics.RenderWindow;
import dev.hilligans.ourcraft.Container.Container;

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
