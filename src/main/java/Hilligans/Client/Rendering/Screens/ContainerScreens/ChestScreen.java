package Hilligans.Client.Rendering.Screens.ContainerScreens;

import Hilligans.Client.Client;
import Hilligans.Client.MatrixStack;
import Hilligans.Client.Rendering.ContainerScreen;
import Hilligans.Client.Rendering.Renderer;
import Hilligans.Container.Containers.ChestContainer;
import Hilligans.Client.Rendering.Textures;
import Hilligans.Util.Settings;

public class ChestScreen extends ContainerScreen<ChestContainer> {

    public ChestScreen(Client client) {
        super(client);
    }

    @Override
    public ChestContainer getContainer() {
        return new ChestContainer();
    }

    @Override
    public void drawScreen(MatrixStack matrixStack) {
        Textures.CHEST.drawCenteredTexture(matrixStack,0,0,158,162, Settings.guiSize);
    }
}
