package Hilligans.Client.Rendering.Screens.ContainerScreens;

import Hilligans.Block.Block;
import Hilligans.Block.Blocks;
import Hilligans.Client.ClientData;
import Hilligans.Client.MatrixStack;
import Hilligans.Client.Rendering.ContainerScreen;
import Hilligans.Client.Rendering.ScreenBase;
import Hilligans.Client.Rendering.Renderer;
import Hilligans.ClientMain;
import Hilligans.Container.Container;
import Hilligans.Container.Containers.InventoryContainer;
import Hilligans.Data.Other.Textures;
import Hilligans.Item.ItemStack;
import Hilligans.Util.Settings;

public class InventoryScreen extends ContainerScreen<InventoryContainer> {

    @Override
    public InventoryContainer getContainer() {
        return new InventoryContainer();
    }

    @Override
    public void drawScreen(MatrixStack matrixStack) {
        //super.render(matrixStack);


        //Renderer.drawTexture(Textures.INVENTORY,0,0,256,256,0,0,157,98);
        Renderer.drawCenteredTexture(matrixStack, Textures.INVENTORY,0,0,158,99,Settings.guiSize);


        //Renderer.drawCenteredTexture(Textures.INVENTORY,4.0f);
    }

    public static void drawHotbar(MatrixStack matrixStack) {
        int width = (int) (16 * Settings.guiSize);
        int startX = (int) (ClientMain.windowX / 2 - width * 4.5f);
        int startY = (int) (ClientMain.windowY - width - 1 * Settings.guiSize);

        Renderer.drawCenteredXTexture(matrixStack, Textures.HOTBAR,startY, Settings.guiSize);

        //Renderer.drawTexture(matrixStack,Textures.ITEM_OUTLINE, (int) (startX),startY,(int)(width + 6 * Settings.guiSize),(int)(width + 6 * Settings.guiSize),5,5,27,27);
        //Renderer

        for(int x = 0; x < 9; x++) {
            ItemStack itemStack = ClientData.inventory.getItem(x);
            //Renderer.drawTexture1(screenStack, ClientData.itemSlot,startX + x * width, startY, width,width);
            if(itemStack.item != null) {
                Block block = Blocks.MAPPED_BLOCKS.get(itemStack.item.name);
                if(block != null) {
                    itemStack.item.render(matrixStack,startX + x * width, startY, width / 2,itemStack);
                }
            }
        }
        Renderer.drawTexture(matrixStack,Textures.ITEM_OUTLINE, (int) (startX - 1 * Settings.guiSize) + width * ClientData.handSlot ,(int)(startY - 1 * Settings.guiSize),(int)(width + 2 * Settings.guiSize),(int)(width + 2 * Settings.guiSize),7,7,25,25);

    }
}
