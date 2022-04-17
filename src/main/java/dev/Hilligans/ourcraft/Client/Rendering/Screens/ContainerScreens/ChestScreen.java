package dev.Hilligans.ourcraft.Client.Rendering.Screens.ContainerScreens;

import dev.Hilligans.ourcraft.Client.Client;
import dev.Hilligans.ourcraft.Client.MatrixStack;
import dev.Hilligans.ourcraft.Client.Rendering.ContainerScreen;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.RenderWindow;
import dev.Hilligans.ourcraft.Container.Containers.ChestContainer;
import dev.Hilligans.ourcraft.Client.Rendering.Textures;
import dev.Hilligans.ourcraft.Util.Settings;

public class ChestScreen extends ContainerScreen<ChestContainer> {

    public ChestScreen(Client client) {
        super(client);
    }

    @Override
    public ChestContainer getContainer() {
        return new ChestContainer();
    }

    @Override
    public void drawScreen(RenderWindow window, MatrixStack matrixStack) {
        Textures.CHEST.drawCenteredTexture(window, matrixStack,0,0,158,162, Settings.guiSize);
    }
}
