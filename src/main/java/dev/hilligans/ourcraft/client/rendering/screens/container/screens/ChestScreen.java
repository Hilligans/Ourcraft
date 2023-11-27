package dev.hilligans.ourcraft.client.rendering.screens.container.screens;

import dev.hilligans.ourcraft.client.Client;
import dev.hilligans.ourcraft.client.MatrixStack;
import dev.hilligans.ourcraft.client.rendering.ContainerScreen;
import dev.hilligans.ourcraft.client.rendering.graphics.RenderWindow;
import dev.hilligans.ourcraft.container.containers.ChestContainer;
import dev.hilligans.ourcraft.client.rendering.Textures;
import dev.hilligans.ourcraft.util.Settings;

public class ChestScreen extends ContainerScreen<ChestContainer> {

    public ChestScreen() {
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
