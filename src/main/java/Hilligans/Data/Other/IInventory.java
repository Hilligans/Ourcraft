package Hilligans.Data.Other;

import Hilligans.Item.Item;
import Hilligans.Item.ItemStack;
import Hilligans.Server.IInventoryChanged;

import java.util.ArrayList;

public interface IInventory {

    int getSize();

    ItemStack getItem(int slot);

    void setItem(int slot, ItemStack item);

    boolean addItem(ItemStack itemStack);

}
