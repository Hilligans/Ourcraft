package Hilligans.Container;

import Hilligans.Client.MatrixStack;
import Hilligans.Client.Rendering.Renderer;
import Hilligans.Data.Other.IInventory;
import Hilligans.Data.Other.Inventory;
import Hilligans.Item.Item;
import Hilligans.Item.ItemStack;
import Hilligans.Network.Packet.Server.SUpdateContainer;
import Hilligans.Network.ServerNetworkHandler;
import Hilligans.Server.IInventoryChanged;
import Hilligans.Util.Settings;

import java.util.Set;

public class Slot implements IInventoryChanged {

    public Inventory inventory;
    public int index;
    public short id;
    public Container container;
    public int startX;
    public int startY;
    public int x = 0;
    public int y = 0;

    public Slot(int startX, int startY, IInventory inventory, int index) {
        this.startX = startX;
        this.startY = startY;
        this.inventory = (Inventory) inventory;
        this.index = index;
    }

    public Slot setContainerAndId(short id, Container container) {
        this.id = id;
        this.container = container;
        inventory.listeners[index].add(this);
        return this;
    }

    public void render(MatrixStack matrixStack) {
        ItemStack itemStack = inventory.getItem(index);
        if(!itemStack.isEmpty()) {
            itemStack.item.render(matrixStack,x,y, (int) (8 * Settings.guiSize),itemStack);
        }
    }

    public boolean canItemBeAdded(ItemStack itemStack) {
        return true;
    }

    public ItemStack swapItemStacks(ItemStack itemStack) {
        ItemStack itemStack1 = inventory.getItem(index);
        byte startCount = itemStack.count;
        itemStack = itemStack1.mergeStack(itemStack);
        if(itemStack.count == startCount) {
            setContents(itemStack);
            return itemStack1;
        } else {
            inventory.notifyListeners(index);
            return itemStack;
        }
    }

    public ItemStack splitStack() {
        if(!getContents().isEmpty()) {
            ItemStack newStack = getContents().splitStack();
            inventory.notifyListeners(index);
            return newStack;
        }
        return ItemStack.emptyStack();
    }

    public boolean canAdd(int count, ItemStack itemStack) {
        if(canItemBeAdded(itemStack)) {
            if(getContents().canAdd(count,itemStack.item)) {
                inventory.notifyListeners(index);
                return true;
            }
        }
        return false;
    }

    public void setContents(ItemStack itemStack) {
        inventory.setItem(index,itemStack);
    }

    public ItemStack getContents() {
        return inventory.getItem(index);
    }

    @Override
    public void onChange(int slot, IInventory inventory) {
        ServerNetworkHandler.sendPacket(new SUpdateContainer((byte) id,getContents(), container.uniqueId),container.channelId);
    }

    public Slot copy() {
        return new Slot(startX,startY,inventory,index);
    }

    public void onClose() {
        inventory.listeners[index].remove(this);
    }
}
