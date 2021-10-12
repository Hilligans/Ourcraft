package dev.Hilligans.Ourcraft.Block.BlockTypes;

import dev.Hilligans.Ourcraft.Block.Block;
import dev.Hilligans.Ourcraft.Client.MatrixStack;
import dev.Hilligans.Ourcraft.Client.Rendering.Renderer;
import dev.Hilligans.Ourcraft.Data.Other.BlockProperties;
import dev.Hilligans.Ourcraft.Data.Other.BlockShapes.BlockShape;
import dev.Hilligans.Ourcraft.Item.ItemStack;

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
