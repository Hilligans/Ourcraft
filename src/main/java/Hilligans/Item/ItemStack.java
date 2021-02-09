package Hilligans.Item;

public class ItemStack {

    public byte count;
    public Item item;

    public ItemStack(Item item, byte count) {
        this.item = item;
        this.count = count;
    }

    public int addItem(ItemStack itemStack) {
        if(item == null) {
            item = itemStack.item;
        }
        if(itemStack.item == item) {
            int itemCount = Math.min(64 - count,itemStack.count);
            count += itemCount;
            return itemCount;
        }
        return 0;
    }

    @Override
    public String toString() {
        return "ItemStack{" +
                "count=" + count +
                ", item=" + item +
                '}';
    }

    public boolean isEmpty() {
        return item == null || count == 0;
    }
}
