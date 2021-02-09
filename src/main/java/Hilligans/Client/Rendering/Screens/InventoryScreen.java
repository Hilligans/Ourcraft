package Hilligans.Client.Rendering.Screens;

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

public class InventoryScreen extends ContainerScreen<InventoryContainer> {

    @Override
    public InventoryContainer getContainer() {
        return new InventoryContainer();
    }

    @Override
    public void drawScreen(MatrixStack matrixStack) {
        //super.render(matrixStack);

        matrixStack.applyTransformation();

        int size = 4;

        //Renderer.drawTexture(Textures.INVENTORY,0,0,256,256,0,0,157,98);
        Renderer.drawCenteredTexture(Textures.INVENTORY,0,0,158,99,size);
/*
        int startX = ClientMain.windowX / 2 - 316;
        int startY = ClientMain.windowY / 2 - 198;


        for(int y = 0; y < 3; y++) {
            for (int x = 0; x < 9; x++) {
                ItemStack itemStack = ClientData.inventory.getItem(9 + x + y * 9);
                if(!itemStack.isEmpty()) {
                    itemStack.item.render(matrixStack, startX + x * 64 + 7 * size, startY + y * 64 + 7 * size, 32, itemStack);
                }
            }
        }

        for(int x = 0; x < 9; x++) {
            ItemStack itemStack = ClientData.inventory.getItem(x);
            if(!itemStack.isEmpty()) {
                itemStack.item.render(matrixStack, startX + x * 64 + 7 * size, startY + 256 + 12 * size, 32, itemStack);
            }
        }

 */


        //Renderer.drawCenteredTexture(Textures.INVENTORY,4.0f);
    }

    public static void drawHotbar(MatrixStack matrixStack) {

        int width = 64;
        int startX = (int) (ClientMain.windowX / 2 - width * 4.5f);
        int startY = ClientMain.windowY - width;

        Renderer.drawCenteredXTexture(Textures.HOTBAR,startY,4.0f);

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
    }
}
