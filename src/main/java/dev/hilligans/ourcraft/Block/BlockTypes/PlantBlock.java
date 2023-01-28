package dev.hilligans.ourcraft.Block.BlockTypes;

import dev.hilligans.ourcraft.Block.Block;
import dev.hilligans.ourcraft.Client.MatrixStack;
import dev.hilligans.ourcraft.Client.Rendering.Renderer;
import dev.hilligans.ourcraft.Data.Other.BlockProperties;
import dev.hilligans.ourcraft.Data.Other.BlockShapes.BlockShape;
import dev.hilligans.ourcraft.Item.ItemStack;

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
