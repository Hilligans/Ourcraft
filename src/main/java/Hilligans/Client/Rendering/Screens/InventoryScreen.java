package Hilligans.Client.Rendering.Screens;

import Hilligans.Block.Block;
import Hilligans.Block.Blocks;
import Hilligans.Client.ClientData;
import Hilligans.Client.MatrixStack;
import Hilligans.Client.Rendering.ScreenBase;
import Hilligans.Client.Rendering.World.Renderer;
import Hilligans.ClientMain;
import Hilligans.Data.Other.Textures;
import Hilligans.Item.ItemStack;

public class InventoryScreen extends ScreenBase {


    @Override
    public void render(MatrixStack matrixStack) {
        super.render(matrixStack);

        matrixStack.applyTransformation();


        //Renderer.drawTexture(Textures.INVENTORY,0,0,256,256,0,0,157,98);
        Renderer.drawCenteredTexture(Textures.INVENTORY,0,0,158,99,4.0f);

        int startX = ClientMain.windowX / 2 - 316;
        int startY = ClientMain.windowY / 2 - 198;


        for(int y = 0; y < 3; y++) {
            for (int x = 0; x < 9; x++) {
                ItemStack itemStack = ClientData.inventory.getItem(9 + x + y * 9);
                if(!itemStack.isEmpty()) {
                    itemStack.item.render(matrixStack, startX + x * 64 + 7 * 4, startY + y * 64 + 7 * 4, 32, itemStack);
                }
            }
        }


        //Renderer.drawCenteredTexture(Textures.INVENTORY,4.0f);
    }
}
