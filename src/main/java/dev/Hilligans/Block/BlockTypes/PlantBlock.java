package dev.Hilligans.Block.BlockTypes;

import dev.Hilligans.Block.Block;
import dev.Hilligans.Client.MatrixStack;
import dev.Hilligans.Client.Rendering.Renderer;
import dev.Hilligans.Data.Other.BlockProperties;
import dev.Hilligans.Data.Other.BlockShapes.BlockShape;
import dev.Hilligans.Item.ItemStack;

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
