package Hilligans.Item;

public class ItemStack {

    public byte count;
    public Item item;

    public ItemStack(Item item, byte count) {
        this.item = item;
        this.count = count;
    }

    public int addItem(ItemStack itemStack) {
        if(itemStack.item == item) {
            int itemCount = Math.min(64 - count,itemStack.count);
            count += itemCount;
            return itemCount;
        }
        return 0;
    }

}
