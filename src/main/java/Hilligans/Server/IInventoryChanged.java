package Hilligans.Server;

import Hilligans.Data.Other.IInventory;

public interface IInventoryChanged {

    void onChange(int slot, IInventory inventory);

}
