package Hilligans.Block.BlockTypes;

import Hilligans.Block.Block;
import Hilligans.Client.MatrixStack;
import Hilligans.Client.Rendering.Renderer;
import Hilligans.Data.Other.BlockProperties;
import Hilligans.Data.Other.BlockShapes.BlockShape;
import Hilligans.Item.ItemStack;

public class PlantBlock extends Block {
    public PlantBlock(String name, BlockProperties blockProperties) {
        super(name, blockProperties);
        blockShape = new BlockShape("xBlock.txt");
        blockProperties.transparent().canWalkThrough();
    }

    @Override
    public void renderItem(MatrixStack matrixStack, int x, int y, int size, ItemStack itemStack) {
        Renderer.renderItem(matrixStack,x,y,size,blockProperties.blockTextureManager);
    }
}
