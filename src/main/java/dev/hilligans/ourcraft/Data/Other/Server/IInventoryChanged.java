package dev.hilligans.ourcraft.Data.Other.Server;

import dev.hilligans.ourcraft.Data.Other.IInventory;

public interface IInventoryChanged {

    void onChange(int slot, IInventory inventory);

}
