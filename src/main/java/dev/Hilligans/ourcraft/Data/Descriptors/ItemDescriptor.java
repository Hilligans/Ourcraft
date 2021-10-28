package dev.Hilligans.ourcraft.Data.Descriptors;

import dev.Hilligans.ourcraft.Item.Item;

public class ItemDescriptor extends Descriptor<Item> {




    public ItemDescriptor() {

    }

    @Override
    public long compare(Item item, BitPriority bitPriority) {
        long score = 0;
        if(name != null) {
            if(!name.equals(item.name)) {
                score |= bitPriority.name;
            }
        }
        if(modID != null) {
            if(!modID.equals(item.modID)) {
                score |= bitPriority.modID;
            }
        }
        if(tagCollection != null) {
            if (!tagCollection.equals(item.itemProperties.tags)) {
                score |= bitPriority.tags;
            }
        }
        return score;
    }
}
