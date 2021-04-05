package Hilligans.World.DataProviders;

import Hilligans.Data.Other.Inventory;
import Hilligans.Tag.CompoundTag;
import Hilligans.World.DataProvider;

public class SlabChestDataProvider extends DataProvider {

    public Inventory inventory = new Inventory(3);

    @Override
    public void read(CompoundTag tag) {
        for(int x = 0; x < 3; x++) {
            inventory.setItem(x,tag.readStack(x));
        }
        super.read(tag);
    }

    @Override
    public void write(CompoundTag tag) {
        for(int x = 0; x < 3; x++) {
            tag.writeStack(x,inventory.getItem(x));
        }
        super.write(tag);
    }

}
