package Hilligans.Client.Rendering.Screens.ContainerScreens;

import Hilligans.Client.Client;
import Hilligans.Client.MatrixStack;
import Hilligans.Client.Rendering.ContainerScreen;
import Hilligans.Client.Rendering.Renderer;
import Hilligans.Client.Rendering.ScreenBase;
import Hilligans.Client.Rendering.Textures;
import Hilligans.Container.Containers.CreativeContainer;
import Hilligans.Util.Settings;

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
