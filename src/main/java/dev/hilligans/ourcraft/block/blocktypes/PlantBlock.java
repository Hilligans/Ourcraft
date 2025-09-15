package dev.hilligans.ourcraft.block.blocktypes;

import dev.hilligans.engine.GameInstance;
import dev.hilligans.ourcraft.block.Block;
import dev.hilligans.engine.client.graphics.resource.MatrixStack;
import dev.hilligans.ourcraft.client.rendering.Renderer;
import dev.hilligans.ourcraft.data.other.BlockProperties;
import dev.hilligans.ourcraft.data.other.blockshapes.BlockShape;
import dev.hilligans.ourcraft.item.ItemStack;

public class PlantBlock extends Block {
    public PlantBlock(String name, BlockProperties blockProperties) {
        super(name, blockProperties);
        blockProperties.transparent().canWalkThrough();
    }

    @Override
    public void renderItem(MatrixStack matrixStack, int x, int y, int size, ItemStack itemStack) {
        Renderer.renderItem(matrixStack,x,y,size);
    }

    @Override
    public void load(GameInstance gameInstance) {
        super.load(gameInstance);
        blockProperties.blockShape = new BlockShape(gameInstance, "xBlock.txt");
    }
}

