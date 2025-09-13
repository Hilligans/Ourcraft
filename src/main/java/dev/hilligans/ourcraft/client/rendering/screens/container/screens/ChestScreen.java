package dev.hilligans.ourcraft.client.rendering.screens.container.screens;

import dev.hilligans.engine.client.graphics.MatrixStack;
import dev.hilligans.ourcraft.client.rendering.ContainerScreen;
import dev.hilligans.ourcraft.client.rendering.Textures;
import dev.hilligans.engine.client.graphics.RenderWindow;
import dev.hilligans.engine.client.graphics.api.GraphicsContext;
import dev.hilligans.ourcraft.container.containers.ChestContainer;
import dev.hilligans.ourcraft.util.Settings;

public class ChestScreen extends ContainerScreen<ChestContainer> {

    public ChestScreen() {
    }

    @Override
    public ChestContainer getContainer() {
        return new ChestContainer(getClient());
    }

    @Override
    public void drawScreen(RenderWindow window, MatrixStack matrixStack, GraphicsContext graphicsContext) {
        Textures.CHEST.drawCenteredTexture(window, graphicsContext, matrixStack,0,0,158,162, Settings.guiSize);
    }
}
