package dev.Hilligans.Client.Rendering.Screens.ContainerScreens;

import dev.Hilligans.Client.Client;
import dev.Hilligans.Client.MatrixStack;
import dev.Hilligans.Client.Rendering.ContainerScreen;
import dev.Hilligans.Container.Container;

public abstract class CustomContainerScreen extends ContainerScreen<Container> {

    public CustomContainerScreen(Client client) {
        super(client);
    }

    @Override
    public abstract void drawScreen(MatrixStack matrixStack);

    @Override
    public Container getContainer() {
        return null;
    }
}
