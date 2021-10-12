package dev.Hilligans.Ourcraft.Client.Rendering.Screens.ContainerScreens;

import dev.Hilligans.Ourcraft.Client.Client;
import dev.Hilligans.Ourcraft.Client.MatrixStack;
import dev.Hilligans.Ourcraft.Client.Rendering.ContainerScreen;
import dev.Hilligans.Ourcraft.Container.Containers.ChestContainer;
import dev.Hilligans.Ourcraft.Client.Rendering.Textures;
import dev.Hilligans.Ourcraft.Util.Settings;

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
