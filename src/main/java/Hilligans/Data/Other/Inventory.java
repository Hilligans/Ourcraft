package Hilligans.Data.Other;

import Hilligans.Block.Blocks;
import Hilligans.Item.Item;
import Hilligans.Item.ItemStack;
import Hilligans.Item.Items;
import Hilligans.Network.Packet.IFuturePacket;
import Hilligans.Network.Packet.Server.SUpdateInventory;
import Hilligans.Network.PacketBase;
import Hilligans.Network.PacketData;
import Hilligans.Network.ServerNetworkHandler;

import java.util.Arrays;

public class Inventory implements IInventory {

    ItemStack[] items;
    public int age;

    public Inventory(int size) {
        items = new ItemStack[size];
        for(int x = 0; x < size; x++) {
            items[x] = new ItemStack(null,(byte)0);
        }
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

    public void writeData(PacketData packetData) {
        packetData.writeInt(age);
        packetData.writeInt(items.length);
        for (ItemStack item : items) {
            packetData.writeItemStack(item);
        }
    }

    public void readData(PacketData packetData) {
        int age = packetData.readInt();
        if(age > this.age) {
            int size = packetData.readInt();
            for (int x = 0; x < size; x++) {
                items[x] = packetData.readItemStack();
            }
        }
        //System.out.println(this.toString());
    }

    @Override
    public String toString() {
        return "Inventory{" +
                "items=" + Arrays.toString(items) +
                ", age=" + age +
                '}';
    }
}
