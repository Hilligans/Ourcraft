package Hilligans.Entity.Entities;

import Hilligans.Blocks.Block;
import Hilligans.Client.MatrixStack;
import Hilligans.Entity.Entity;

public class ItemEntity extends Entity {

    Block block;

    public ItemEntity(float x, float y, float z, int id, Block block) {
        super(x, y, z, id);
        this.block = block;
    }

    @Override
    public void render(MatrixStack matrixStack) {

    }
}
