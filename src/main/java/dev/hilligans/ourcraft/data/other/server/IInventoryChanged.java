package dev.hilligans.ourcraft.data.other.server;

import dev.hilligans.ourcraft.data.other.IInventory;

public interface IInventoryChanged {

    void onChange(int slot, IInventory inventory);

}
