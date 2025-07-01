package dev.hilligans.ourcraft.data.other;

import dev.hilligans.ourcraft.data.other.server.IInventoryChanged;
import dev.hilligans.ourcraft.item.ItemStack;

public interface IInventory {

    int getInventorySize();

    ItemStack getItem(int slot);

    void setItem(int slot, ItemStack item);

    /**
     * Inserts the item into the first open slot in the inventory, the itemstack can be combined with existing item stacks if needed
     * @param itemStack the itemstack to be added to the inventory
     * @return true if the entire stack was able to be added, false otherwise
     */
    boolean addItem(ItemStack itemStack);

    void addListener(int slot, IInventoryChanged iInventoryChanged);

    void removeListener(int slot, IInventoryChanged iInventoryChanged);

    void notifyListeners(int slot);
}
