package dev.hilligans.ourcraft.world.data.providers;

import dev.hilligans.ourcraft.data.other.Inventory;
import dev.hilligans.ourcraft.tag.CompoundNBTTag;
import dev.hilligans.ourcraft.world.DataProvider;

public class ChestDataProvider extends DataProvider {

    public Inventory inventory = new Inventory(27);

    @Override
    public void read(CompoundNBTTag tag) {
        for(int x = 0; x < 27; x++) {
            inventory.setItem(x,tag.readStack(x));
        }
        super.read(tag);
    }

    @Override
    public void write(CompoundNBTTag tag) {
        for(int x = 0; x < 27; x++) {
            tag.writeStack(x,inventory.getItem(x));
        }
        super.write(tag);
    }
}
