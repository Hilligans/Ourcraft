package dev.hilligans.ourcraft.data.descriptors;

import dev.hilligans.ourcraft.item.Item;

public class ItemDescriptor extends Descriptor<Item> {




    public ItemDescriptor() {

    }

    public ItemDescriptor(boolean matchesExactly) {
        this.matchesExactly = matchesExactly;
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
