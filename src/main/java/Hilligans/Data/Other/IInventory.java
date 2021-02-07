package Hilligans.Data.Other;

import Hilligans.Item.Item;
import Hilligans.Item.ItemStack;

public interface IInventory {

    int getSize();

    ItemStack getItem(int slot);

    void setItem(int slot, ItemStack item);

}
