package Hilligans.Data.Other.Server;

import Hilligans.Data.Other.IInventory;

public interface IInventoryChanged {

    public void onChange(int slot, IInventory inventory);

}
