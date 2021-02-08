package Hilligans.Client.Rendering.Screens;

import Hilligans.Block.Block;
import Hilligans.Block.Blocks;
import Hilligans.Client.ClientData;
import Hilligans.Client.MatrixStack;
import Hilligans.Client.Rendering.ScreenBase;
import Hilligans.Client.Rendering.World.Renderer;
import Hilligans.Item.ItemStack;

public class InventoryScreen extends ScreenBase {


    @Override
    public void render(MatrixStack matrixStack) {
        super.render(matrixStack);



        for(int x = 0; x < 9; x++) {
            ItemStack itemStack = ClientData.inventory.getItem(x);

            if(itemStack.item != null) {
                Block block = Blocks.MAPPED_BLOCKS.get(itemStack.item.name);
                if(block != null) {
                    Renderer.renderBlockItem(matrixStack,x * 72, 300, 32,block);
                    //Renderer.drawTexture1(matrixStack, block.blockTextureManager.texture, x * 72, 300, 64, 64);
                }
            }
        }
    }
}
