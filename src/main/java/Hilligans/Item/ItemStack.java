package Hilligans.Item;

public class ItemStack {

    public byte count;
    public Item item;

    public ItemStack(Item item, byte count) {
        this.item = item;
        this.count = count;
    }

}
