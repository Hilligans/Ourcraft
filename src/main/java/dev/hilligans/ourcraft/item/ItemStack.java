package dev.hilligans.ourcraft.item;

import dev.hilligans.ourcraft.client.MatrixStack;
import dev.hilligans.ourcraft.tag.CompoundNBTTag;
import dev.hilligans.ourcraft.tag.EndNBTTag;
import dev.hilligans.ourcraft.tag.NBTTag;
import dev.hilligans.ourcraft.util.Settings;

public class ItemStack {

    public int count;
    public Item item;
    public dev.hilligans.ourcraft.tag.NBTTag NBTTag;

    public ItemStack(Item item, int count) {
        this.item = item;
        this.count = count;
        NBTTag = new EndNBTTag();
    }

    public ItemStack(Item item, int count, CompoundNBTTag compoundTag) {
        this.item = item;
        this.count = count;
        this.NBTTag = compoundTag;
    }

    public ItemStack(Item item, int count, NBTTag compoundTag) {
        this.item = item;
        this.count = count;
        this.NBTTag = compoundTag;
    }

    /**
     *
     * @param itemStack the itemStack which we are taking items from
     * @return returns the amount of items taken from itemStack
     */
    public int addItem(ItemStack itemStack) {
        if(!itemStack.isEmpty()) {
            if (item == null) {
                item = itemStack.item;
            }
            if (itemStack.item == item) {
                int itemCount = Math.min(64 - count, itemStack.count);
                count += itemCount;
                return itemCount;
            }
        }
        return 0;
    }

    public ItemStack add(int amount) {
        return setCount((byte) Math.min(count + amount, 64));
    }

    public ItemStack setCount(byte count) {
        this.count = count;
        return this;
    }

    public String getName() {
        return item == null ? "" : item.name;
    }

    public ItemStack mergeStack(ItemStack itemStack) {
        int leftOver = itemStack.count - addItem(itemStack);
        if(leftOver == 0) {
            return emptyStack();
        } else {
            itemStack.count = (byte) leftOver;
        }
        return itemStack;
    }

    public boolean canAdd(int count, Item item) {
        if(isEmpty()) {
            this.item = item;
            this.count = (byte) count;
            return true;
        }
        if(item == this.item && this.count + count <= 64) {
            this.count += count;
            return true;
        }
        return false;
    }

    public ItemStack splitStack() {
        byte count = (byte) (Math.ceil(this.count / 2f));
        this.count -= count;
        ItemStack itemStack = new ItemStack(item,count);
        if(this.count == 0) {
            this.item = null;
        }
        return itemStack;
    }

    public void removeCount(int count) {
        this.count -= count;
        if(this.count <= 0) {
            this.item  = null;
            this.count = 0;
        }
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

    public void renderStack(MatrixStack matrixStack, int x, int y) {
        if(!isEmpty()) {
            item.render(matrixStack,x,y, (int) (Settings.guiSize * 8), this);
        }
    }

    public ItemStack copy() {
        return new ItemStack(item,count,NBTTag == null ? null : NBTTag.duplicate());
    }

    public static ItemStack emptyStack() {
        return new ItemStack(null,(byte)0);
    }
}
