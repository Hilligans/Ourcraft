package Hilligans.Client.Rendering.Screens.ContainerScreens;

import Hilligans.Client.Client;
import Hilligans.Client.MatrixStack;
import Hilligans.Client.Rendering.ContainerScreen;
import Hilligans.Client.Rendering.Renderer;
import Hilligans.Client.Rendering.Texture;
import Hilligans.Client.Rendering.Textures;
import Hilligans.Container.Container;
import Hilligans.Util.Settings;

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
