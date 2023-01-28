package dev.hilligans.ourcraft.Client.Rendering.Screens.ContainerScreens;

import dev.hilligans.ourcraft.Client.Client;
import dev.hilligans.ourcraft.Client.MatrixStack;
import dev.hilligans.ourcraft.Client.Rendering.ContainerScreen;
import dev.hilligans.ourcraft.Client.Rendering.Graphics.RenderWindow;
import dev.hilligans.ourcraft.Client.Rendering.Textures;
import dev.hilligans.ourcraft.Container.Containers.CreativeContainer;
import dev.hilligans.ourcraft.Util.Settings;

public class CreativeInventoryScreen extends ContainerScreen<CreativeContainer> {
    public CreativeInventoryScreen(Client client) {
        super(client);
    }

    @Override
    public CreativeContainer getContainer() {
        return new CreativeContainer();
    }

    @Override
    public void drawScreen(RenderWindow window, MatrixStack matrixStack) {
        Textures.CREATIVE_INVENTORY.drawCenteredTexture(window, matrixStack,0,0,158,210, Settings.guiSize);
    }
}
