package dev.hilligans.ourcraft.data.other;

import dev.hilligans.ourcraft.data.other.server.IInventoryChanged;
import dev.hilligans.ourcraft.item.ItemStack;

public class JoinedInventory implements IInventory {

    IInventory[] inventories;

    public JoinedInventory(IInventory... inventories) {
        this.inventories = inventories;
    }

    @Override
    public int getInventorySize() {
        int size = 0;
        for(IInventory iInventory : inventories) {
            size += iInventory.getInventorySize();
        }
        return size;
    }

    @Override
    public ItemStack getItem(int slot) {
        for(IInventory iInventory : inventories) {
            if(slot >= iInventory.getInventorySize()) {
                slot -= iInventory.getInventorySize();
            } else {
                return iInventory.getItem(slot);
            }
        }
        return ItemStack.emptyStack();
    }

    @Override
    public void setItem(int slot, ItemStack item) {
        for(IInventory iInventory : inventories) {
            if(slot >= iInventory.getInventorySize()) {
                slot -= iInventory.getInventorySize();
            } else {
                iInventory.setItem(slot,item);
                break;
            }
        }
    }

    @Override
    public boolean addItem(ItemStack itemStack) {
        for(IInventory iInventory : inventories) {
            if(iInventory.addItem(itemStack)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void addListener(int slot, IInventoryChanged iInventoryChanged) {
        for(IInventory iInventory : inventories) {
            if(slot >= iInventory.getInventorySize()) {
                slot -= iInventory.getInventorySize();
            } else {
                iInventory.addListener(slot,iInventoryChanged);
                break;
            }
        }
    }

    @Override
    public void removeListener(int slot, IInventoryChanged iInventoryChanged) {
        for(IInventory iInventory : inventories) {
            if(slot >= iInventory.getInventorySize()) {
                slot -= iInventory.getInventorySize();
            } else {
                iInventory.removeListener(slot,iInventoryChanged);
                break;
            }
        }
    }

    @Override
    public void notifyListeners(int slot) {
        for(IInventory iInventory : inventories) {
            if(slot >= iInventory.getInventorySize()) {
                slot -= iInventory.getInventorySize();
            } else {
                iInventory.notifyListeners(slot);
                break;
            }
        }
    }
}
