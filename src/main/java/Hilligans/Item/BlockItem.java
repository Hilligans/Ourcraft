package Hilligans.Item;

import Hilligans.Block.Block;
import Hilligans.Client.MatrixStack;
import Hilligans.Client.Rendering.World.Renderer;

public class BlockItem extends Item {

    public Block block;

    public BlockItem(String name, Block block) {
        super(name);
        this.block = block;
    }

    @Override
    public void render(MatrixStack matrixStack, int x, int y, int size, ItemStack itemStack) {
        Renderer.renderBlockItem(matrixStack,x,y,size,block);
        drawString(matrixStack,x - size / 2,y,size,itemStack.count);
    }
}
