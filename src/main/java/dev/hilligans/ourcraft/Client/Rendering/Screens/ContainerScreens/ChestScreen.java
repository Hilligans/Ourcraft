package dev.hilligans.ourcraft.Client.Rendering.Screens.ContainerScreens;

import dev.hilligans.ourcraft.Client.Client;
import dev.hilligans.ourcraft.Client.MatrixStack;
import dev.hilligans.ourcraft.Client.Rendering.ContainerScreen;
import dev.hilligans.ourcraft.Client.Rendering.Graphics.RenderWindow;
import dev.hilligans.ourcraft.Container.Containers.ChestContainer;
import dev.hilligans.ourcraft.Client.Rendering.Textures;
import dev.hilligans.ourcraft.Util.Settings;

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
