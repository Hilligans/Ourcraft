package dev.Hilligans.ourcraft.Client.Rendering.Screens.ContainerScreens;

import dev.Hilligans.ourcraft.Client.Client;
import dev.Hilligans.ourcraft.Client.MatrixStack;
import dev.Hilligans.ourcraft.Client.Rendering.ContainerScreen;
import dev.Hilligans.ourcraft.Client.Rendering.Textures;
import dev.Hilligans.ourcraft.Container.Containers.CreativeContainer;
import dev.Hilligans.ourcraft.Util.Settings;

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
       // Textures.CREATIVE_INVENTORY.drawCenteredTexture(matrixStack,0,0,158,210, Settings.guiSize);
    }
}
