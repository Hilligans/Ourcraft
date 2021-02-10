package Hilligans.Item;

import Hilligans.Client.MatrixStack;
import Hilligans.Client.Rendering.World.StringRenderer;
import Hilligans.Entity.LivingEntities.PlayerEntity;
import Hilligans.World.World;

public class Item {

    public String name;
    public int id;

    public Item(String name) {
        this.name = name;
        Items.ITEMS.add(this);
        Items.HASHED_ITEMS.put(name,this);
        id = Items.getNextId();
    }

    public void render(MatrixStack matrixStack,int x, int y, int size, ItemStack itemStack) {}

    public boolean onActivate(World world, PlayerEntity playerEntity) {
        return true;
    }

    void drawString(MatrixStack matrixStack, int x, int y, int size, int count) {
        if(count != 1) {
            if(count >= 10) {
                StringRenderer.drawString(matrixStack, count + "", x + size + 14 , (int) (y + size * 1f), 0.5f);
            } else {
                StringRenderer.drawString(matrixStack, count + "", (int) (x + size ) + 29, (int) (y + size * 1f), 0.5f);
            }
        }
    }


    @Override
    public String toString() {
        return "Item{" +
                "name='" + name + '\'' +
                '}';
    }
}
