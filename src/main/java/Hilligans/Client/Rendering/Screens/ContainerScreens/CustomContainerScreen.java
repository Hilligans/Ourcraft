package Hilligans.Client.Rendering.Screens.ContainerScreens;

import Hilligans.Client.MatrixStack;
import Hilligans.Client.Rendering.ContainerScreen;
import Hilligans.Client.Rendering.Renderer;
import Hilligans.Client.Rendering.Texture;
import Hilligans.Client.Rendering.Textures;
import Hilligans.Container.Container;
import Hilligans.Util.Settings;

public class CustomContainerScreen extends ContainerScreen<Container> {

    String texture;

    public CustomContainerScreen(String textureName) {
        this.texture = textureName;
    }

    @Override
    public void drawScreen(MatrixStack matrixStack) {
        Texture texture1 = Textures.mappedTextures.get(texture);
        if(texture1 != null) {
            Renderer.drawCenteredTexture(matrixStack, texture1, 0, 0, 158, 162, Settings.guiSize);
        }

    }

    @Override
    public Container getContainer() {
        return null;
    }
}
