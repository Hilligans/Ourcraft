package Hilligans.Block.BlockTypes;

import Hilligans.Block.Block;
import Hilligans.Client.MatrixStack;
import Hilligans.Client.Rendering.Renderer;
import Hilligans.Data.Other.BlockProperties;
import Hilligans.Data.Other.BlockShapes.XBlockShape;
import Hilligans.Data.Other.BlockState;
import Hilligans.Item.ItemStack;
import Hilligans.Util.Vector5f;

public class PlantBlock extends Block {
    public PlantBlock(String name, BlockProperties blockProperties) {
        super(name, blockProperties);
        blockShape = new XBlockShape();
        blockProperties.transparent().canWalkThrough();
    }

    @Override
    public void renderItem(MatrixStack matrixStack, int x, int y, int size, ItemStack itemStack) {
        Renderer.renderItem(matrixStack,x,y,size,blockProperties.blockTextureManager);
    }
}
