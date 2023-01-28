package dev.hilligans.ourcraft.Util.GameResource;

import dev.hilligans.ourcraft.Item.Item;

public class ItemGameResource extends GameResource {

    public Item item;

    public ItemGameResource(Item item) {
        this.item = item;
    }

    @Override
    public String toString() {
        return "ItemGameResource{" +
                "item=" + item.getUniqueName() +
                '}';
    }
}
