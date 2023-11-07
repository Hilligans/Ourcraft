package dev.hilligans.ourcraft.client.rendering.screens.container.screens;

import dev.hilligans.ourcraft.client.Client;
import dev.hilligans.ourcraft.client.MatrixStack;
import dev.hilligans.ourcraft.client.rendering.ContainerScreen;
import dev.hilligans.ourcraft.client.rendering.graphics.RenderWindow;
import dev.hilligans.ourcraft.client.rendering.Textures;
import dev.hilligans.ourcraft.container.containers.CreativeContainer;
import dev.hilligans.ourcraft.util.Settings;

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
