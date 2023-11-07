package dev.hilligans.ourcraft.client.rendering.screens.container.screens;

import dev.hilligans.ourcraft.client.Client;
import dev.hilligans.ourcraft.client.MatrixStack;
import dev.hilligans.ourcraft.client.rendering.ContainerScreen;
import dev.hilligans.ourcraft.client.rendering.graphics.RenderWindow;
import dev.hilligans.ourcraft.ClientMain;
import dev.hilligans.ourcraft.container.containers.InventoryContainer;
import dev.hilligans.ourcraft.client.rendering.Textures;
import dev.hilligans.ourcraft.item.ItemStack;
import dev.hilligans.ourcraft.util.Settings;

public class InventoryScreen extends ContainerScreen<InventoryContainer> {

    public InventoryScreen(Client client) {
        super(client);
    }

    @Override
    public InventoryContainer getContainer() {
        return new InventoryContainer();
    }

    @Override
    public void drawScreen(RenderWindow window, MatrixStack matrixStack) {
        Textures.INVENTORY.drawCenteredTexture(window, matrixStack,0,0,158,99, Settings.guiSize);
    }

    public void drawHotbar(RenderWindow window, MatrixStack matrixStack) {
        int width = (int) (16 * Settings.guiSize);
        int startX = (int) (window.getWindowWidth() / 2 - width * 4.5f);
        int startY = (int) (window.getWindowHeight() - width - 1 * Settings.guiSize);
        window.getGraphicsEngine().getDefaultImpl().uploadMatrix(null,matrixStack,defaultShader);
        //matrixStack.applyTransformation();
       // Textures.HOTBAR.drawCenteredXTexture(matrixStack,startY, Settings.guiSize);

        for(int x = 0; x < 9; x++) {
            ItemStack itemStack = ClientMain.getClient().playerData.inventory.getItem(x);
            if(!itemStack.isEmpty()) {
                itemStack.item.render(matrixStack,startX + x * width, startY, width / 2,itemStack);
            }
        }
       // Textures.ITEM_OUTLINE.drawTexture(matrixStack, (int) (startX - 1 * Settings.guiSize) + width * ClientMain.getClient().playerData.handSlot ,(int)(startY - 1 * Settings.guiSize),(int)(width + 2 * Settings.guiSize),(int)(width + 2 * Settings.guiSize),7,7,25,25);

    }
}
