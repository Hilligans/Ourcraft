package Hilligans.Data.Other;

import Hilligans.Item.ItemStack;

public interface IInventory {

    int getSize();

    ItemStack getItem(int slot);

    void setItem(int slot, ItemStack item);

    boolean addItem(ItemStack itemStack);

}
