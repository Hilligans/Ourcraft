package Hilligans.Client;

import Hilligans.Block.Block;
import Hilligans.Block.Blocks;
import Hilligans.Client.Rendering.Renderer;
import Hilligans.Client.Rendering.World.StringRenderer;

public class BlockPlacer {

    public static int id = 1;

    public static void increase() {
        id++;
        if(id == Blocks.BLOCKS.size()) {
            id = 1;
        }
    }

    public static void decrease() {
        id--;
        if(id == 0) {
            id = Blocks.BLOCKS.size() - 1;
        }
    }

    public static void render(MatrixStack matrixStack) {
        Block block = Blocks.getBlockWithID(id);
        int texture = block.blockTextureManager.texture;
        Renderer.drawTexture1(matrixStack,texture,0,0,64,64);
        StringRenderer.drawString(matrixStack,block.name,78,0,1.0f);
    }



}
