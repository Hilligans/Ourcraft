package dev.hilligans.ourcraft.client.rendering.screens.container.screens;

import dev.hilligans.engine.client.graphics.resource.MatrixStack;
import dev.hilligans.ourcraft.client.rendering.ContainerScreen;
import dev.hilligans.ourcraft.client.rendering.Textures;
import dev.hilligans.engine.client.graphics.RenderWindow;
import dev.hilligans.engine.client.graphics.api.GraphicsContext;
import dev.hilligans.ourcraft.container.containers.CreativeContainer;
import dev.hilligans.ourcraft.util.Settings;

public class CreativeInventoryScreen extends ContainerScreen<CreativeContainer> {

    @Override
    public CreativeContainer getContainer() {
        return new CreativeContainer(getClient());
    }

    @Override
    public void drawScreen(RenderWindow window, MatrixStack matrixStack, GraphicsContext graphicsContext) {
        Textures.CREATIVE_INVENTORY.drawCenteredTexture(window, graphicsContext, matrixStack,0,0,158,210, Settings.guiSize);
    }
}
