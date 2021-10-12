package dev.Hilligans.Ourcraft.Client.Rendering.Screens.ContainerScreens;

import dev.Hilligans.Ourcraft.Client.Client;
import dev.Hilligans.Ourcraft.Client.MatrixStack;
import dev.Hilligans.Ourcraft.Client.Rendering.ContainerScreen;
import dev.Hilligans.Ourcraft.Container.Container;

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
