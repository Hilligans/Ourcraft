package dev.hilligans.ourcraft.block.blocktypes;

import dev.hilligans.ourcraft.block.Block;
import dev.hilligans.ourcraft.client.MatrixStack;
import dev.hilligans.ourcraft.client.rendering.Renderer;
import dev.hilligans.ourcraft.data.other.BlockProperties;
import dev.hilligans.ourcraft.data.other.blockshapes.BlockShape;
import dev.hilligans.ourcraft.item.ItemStack;

public class PlantBlock extends Block {
    public PlantBlock(String name, BlockProperties blockProperties) {
        super(name, blockProperties);
        blockProperties.blockShape = new BlockShape("xBlock.txt");
        blockProperties.transparent().canWalkThrough();
    }

    @Override
    public void renderItem(MatrixStack matrixStack, int x, int y, int size, ItemStack itemStack) {
        Renderer.renderItem(matrixStack,x,y,size,blockProperties.blockTextureManager);
    }
}
