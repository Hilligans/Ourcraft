package Hilligans.Data.Other;

import Hilligans.Item.Item;
import Hilligans.Item.ItemStack;
import Hilligans.Network.Packet.IFuturePacket;
import Hilligans.Network.Packet.Server.SUpdateInventory;
import Hilligans.Network.PacketBase;
import Hilligans.Network.ServerNetworkHandler;

public class Inventory implements IInventory {

    ItemStack[] items;
    public int age;

    public Inventory(int size) {
        items = new ItemStack[size];
    }

    @Override
    public int getSize() {
        return items.length;
    }

    @Override
    public ItemStack getItem(int slot) {
        return items[slot];
    }

    @Override
    public void setItem(int slot, ItemStack item) {
        items[slot] = item;
    }

    public void markDirty() {
        age++;
    }

    @Override
    public boolean addItem(ItemStack itemStack) {
        for(ItemStack itemStack1 : items) {
            int toRemove = itemStack1.addItem(itemStack);
            itemStack.count -= toRemove;
            if(itemStack.count == 0) {
                return true;
            }
        }
        return false;
    }
}
