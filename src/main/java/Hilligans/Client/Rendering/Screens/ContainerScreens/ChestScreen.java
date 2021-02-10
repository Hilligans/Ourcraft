package Hilligans.Client.Rendering.Screens.ContainerScreens;

import Hilligans.Client.MatrixStack;
import Hilligans.Client.Rendering.ContainerScreen;
import Hilligans.Client.Rendering.Renderer;
import Hilligans.Container.Containers.ChestContainer;
import Hilligans.Data.Other.Textures;
import Hilligans.Util.Settings;

public class ChestScreen extends ContainerScreen<ChestContainer> {

    @Override
    public ChestContainer getContainer() {
        return new ChestContainer();
    }

    @Override
    public void drawScreen(MatrixStack matrixStack) {
        Renderer.drawCenteredTexture(matrixStack, Textures.CHEST,0,0,158,162, Settings.guiSize);
    }
}
