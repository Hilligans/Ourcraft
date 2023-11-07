package dev.hilligans.ourcraft.data.other;

import dev.hilligans.ourcraft.data.other.server.IInventoryChanged;
import dev.hilligans.ourcraft.item.ItemStack;

public interface IInventory {

    int getSize();

    ItemStack getItem(int slot);

    void setItem(int slot, ItemStack item);

    boolean addItem(ItemStack itemStack);

    void addListener(int slot, IInventoryChanged iInventoryChanged);

    void removeListener(int slot, IInventoryChanged iInventoryChanged);

    void notifyListeners(int slot);
}
