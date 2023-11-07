package dev.hilligans.ourcraft.util.game.resource;

import dev.hilligans.ourcraft.item.Item;

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
