package dev.Hilligans.ourcraft.Data.Other;

import dev.Hilligans.ourcraft.Data.Other.Server.IInventoryChanged;
import dev.Hilligans.ourcraft.Item.ItemStack;

public interface IInventory {

    int getSize();

    ItemStack getItem(int slot);

    void setItem(int slot, ItemStack item);

    boolean addItem(ItemStack itemStack);

    void addListener(int slot, IInventoryChanged iInventoryChanged);

    void removeListener(int slot, IInventoryChanged iInventoryChanged);

    void notifyListeners(int slot);
}
