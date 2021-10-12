package dev.Hilligans.Ourcraft.Client.Rendering.Screens.ContainerScreens;

import dev.Hilligans.Ourcraft.Client.Client;
import dev.Hilligans.Ourcraft.Client.MatrixStack;
import dev.Hilligans.Ourcraft.Client.Rendering.ContainerScreen;
import dev.Hilligans.Ourcraft.Client.Rendering.Textures;
import dev.Hilligans.Ourcraft.Container.Containers.CreativeContainer;
import dev.Hilligans.Ourcraft.Util.Settings;

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
