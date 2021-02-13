package Hilligans.World.DataProviders;

import Hilligans.Data.Other.IInventory;
import Hilligans.Data.Other.Inventory;
import Hilligans.Tag.CompoundTag;
import Hilligans.World.DataProvider;

public class ChestDataProvider extends DataProvider {

    public Inventory inventory = new Inventory(27);

    @Override
    public void read(CompoundTag tag) {
        for(int x = 0; x < 27; x++) {
            inventory.setItem(x,tag.readStack(x));
        }
        super.read(tag);
    }

    @Override
    public void write(CompoundTag tag) {
        for(int x = 0; x < 27; x++) {
            tag.writeStack(x,inventory.getItem(x));
        }
        super.write(tag);
    }
}
