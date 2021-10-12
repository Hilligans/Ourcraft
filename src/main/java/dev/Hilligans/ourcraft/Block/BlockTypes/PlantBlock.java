package dev.Hilligans.ourcraft.Block.BlockTypes;

import dev.Hilligans.ourcraft.Block.Block;
import dev.Hilligans.ourcraft.Client.MatrixStack;
import dev.Hilligans.ourcraft.Client.Rendering.Renderer;
import dev.Hilligans.ourcraft.Data.Other.BlockProperties;
import dev.Hilligans.ourcraft.Data.Other.BlockShapes.BlockShape;
import dev.Hilligans.ourcraft.Item.ItemStack;

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
