package Hilligans.Container;

import Hilligans.Client.MatrixStack;
import Hilligans.Client.Rendering.Renderer;
import Hilligans.Data.Other.IInventory;
import Hilligans.Item.Item;
import Hilligans.Item.ItemStack;
import Hilligans.Util.Settings;

import java.util.Set;

public class Slot {

    IInventory inventory;
    int index;
    public int x;
    public int y;

    public Slot(int x, int y, IInventory inventory, int index) {
        this.x = x;
        this.y = y;
        this.inventory = inventory;
        this.index = index;
    }

    public void render(MatrixStack matrixStack) {
        ItemStack itemStack = inventory.getItem(index);
        if(!itemStack.isEmpty()) {
            itemStack.item.render(matrixStack,x,y, (int) (8 * Settings.guiSize),itemStack);
        }
    }

    public boolean canItemBeAdded(ItemStack itemStack) {
        return true;
    }

    public ItemStack swapItemStacks(ItemStack itemStack) {
        ItemStack itemStack1 = inventory.getItem(index);
        byte startCount = itemStack.count;
        itemStack = itemStack1.mergeStack(itemStack);
        if(itemStack.count == startCount) {
            setContents(itemStack);
            return itemStack1;
        } else {
            return itemStack;
        }
    }

    public void setContents(ItemStack itemStack) {
        inventory.setItem(index,itemStack);
    }

    public ItemStack getContents() {
        return inventory.getItem(index);
    }
}
