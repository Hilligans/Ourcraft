package dev.Hilligans.Data.Other.Server;

import dev.Hilligans.Data.Other.IInventory;

public interface IInventoryChanged {

    void onChange(int slot, IInventory inventory);

}
