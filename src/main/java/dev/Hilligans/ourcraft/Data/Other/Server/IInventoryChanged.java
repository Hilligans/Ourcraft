package dev.Hilligans.ourcraft.Data.Other.Server;

import dev.Hilligans.ourcraft.Data.Other.IInventory;

public interface IInventoryChanged {

    void onChange(int slot, IInventory inventory);

}
