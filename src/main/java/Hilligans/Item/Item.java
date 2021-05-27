package Hilligans.Item;

import Hilligans.Client.MatrixStack;
import Hilligans.Client.Rendering.World.StringRenderer;
import Hilligans.Data.Other.ItemProperties;
import Hilligans.Entity.LivingEntities.PlayerEntity;
import Hilligans.Ourcraft;
import Hilligans.World.World;

public class Item {

    public String name;
    public ItemProperties itemProperties;
    public int id;
    public String modID;

    public Item(String name, ItemProperties itemProperties) {
        this.name = name;
        this.itemProperties = itemProperties;
        this.modID = Ourcraft.MOD_LOADER.mod;
        if(itemProperties.serverSide) {

        } else {
            Items.ITEMS.add(this);
            Items.HASHED_ITEMS.put(name, this);
        }
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

    public String getName() {
        return "item." + modID + "." + name;
    }


    @Override
    public String toString() {
        return "Item{" +
                "name='" + name + '\'' +
                '}';
    }
}
