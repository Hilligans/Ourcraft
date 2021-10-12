package dev.Hilligans.ourcraft.Client.Rendering.Screens.ContainerScreens;

import dev.Hilligans.ourcraft.Client.Client;
import dev.Hilligans.ourcraft.Client.MatrixStack;
import dev.Hilligans.ourcraft.Client.Rendering.ContainerScreen;
import dev.Hilligans.ourcraft.ClientMain;
import dev.Hilligans.ourcraft.Container.Containers.InventoryContainer;
import dev.Hilligans.ourcraft.Client.Rendering.Textures;
import dev.Hilligans.ourcraft.Item.ItemStack;
import dev.Hilligans.ourcraft.Util.Settings;

public class InventoryScreen extends ContainerScreen<InventoryContainer> {

    public InventoryScreen(Client client) {
        super(client);
    }

    @Override
    public InventoryContainer getContainer() {
        return new InventoryContainer();
    }

    @Override
    public void drawScreen(MatrixStack matrixStack) {
        Textures.INVENTORY.drawCenteredTexture(matrixStack,0,0,158,99,Settings.guiSize);
    }

    public static void drawHotbar(MatrixStack matrixStack) {
        int width = (int) (16 * Settings.guiSize);
        int startX = (int) (ClientMain.getWindowX() / 2 - width * 4.5f);
        int startY = (int) (ClientMain.getWindowY() - width - 1 * Settings.guiSize);
        matrixStack.applyTransformation();
        Textures.HOTBAR.drawCenteredXTexture(matrixStack,startY, Settings.guiSize);

        for(int x = 0; x < 9; x++) {
            ItemStack itemStack = ClientMain.getClient().playerData.inventory.getItem(x);
            if(!itemStack.isEmpty()) {
                itemStack.item.render(matrixStack,startX + x * width, startY, width / 2,itemStack);
            }
        }
        Textures.ITEM_OUTLINE.drawTexture(matrixStack, (int) (startX - 1 * Settings.guiSize) + width * ClientMain.getClient().playerData.handSlot ,(int)(startY - 1 * Settings.guiSize),(int)(width + 2 * Settings.guiSize),(int)(width + 2 * Settings.guiSize),7,7,25,25);

    }
}
