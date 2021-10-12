package dev.Hilligans.Client.Rendering.Screens.ContainerScreens;

import dev.Hilligans.Client.Client;
import dev.Hilligans.Client.MatrixStack;
import dev.Hilligans.Client.Rendering.ContainerScreen;
import dev.Hilligans.Container.Containers.ChestContainer;
import dev.Hilligans.Client.Rendering.Textures;
import dev.Hilligans.Util.Settings;

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
