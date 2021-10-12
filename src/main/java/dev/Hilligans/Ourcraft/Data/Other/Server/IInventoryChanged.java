package dev.Hilligans.Ourcraft.Data.Other.Server;

import dev.Hilligans.Ourcraft.Data.Other.IInventory;

public interface IInventoryChanged {

    void onChange(int slot, IInventory inventory);

}
