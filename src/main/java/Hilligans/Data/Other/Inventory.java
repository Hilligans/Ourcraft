package Hilligans.Data.Other;

import Hilligans.Item.Item;
import Hilligans.Item.ItemStack;

public class Inventory implements IInventory {

    ItemStack[] items;

    public Inventory(int size) {
        items = new ItemStack[size];
    }

    @Override
    public int getSize() {
        return items.length;
    }

    @Override
    public ItemStack getItem(int slot) {
        return items[slot];
    }

    @Override
    public void setItem(int slot, ItemStack item) {
        items[slot] = item;
    }
}
