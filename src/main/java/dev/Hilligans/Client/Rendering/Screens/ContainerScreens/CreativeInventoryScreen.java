package dev.Hilligans.Client.Rendering.Screens.ContainerScreens;

import dev.Hilligans.Client.Client;
import dev.Hilligans.Client.MatrixStack;
import dev.Hilligans.Client.Rendering.ContainerScreen;
import dev.Hilligans.Client.Rendering.Textures;
import dev.Hilligans.Container.Containers.CreativeContainer;
import dev.Hilligans.Util.Settings;

public class CreativeInventoryScreen extends ContainerScreen<CreativeContainer> {
    public CreativeInventoryScreen(Client client) {
        super(client);
    }

    @Override
    public CreativeContainer getContainer() {
        return new CreativeContainer();
    }

    @Override
    public void drawScreen(MatrixStack matrixStack) {
        Textures.CREATIVE_INVENTORY.drawCenteredTexture(matrixStack,0,0,158,210, Settings.guiSize);
    }
}
