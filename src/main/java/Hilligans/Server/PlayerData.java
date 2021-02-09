package Hilligans.Server;

import Hilligans.Container.Container;
import Hilligans.Container.Containers.InventoryContainer;
import Hilligans.Container.Slot;
import Hilligans.Data.Other.Inventory;
import Hilligans.Entity.LivingEntities.PlayerEntity;
import Hilligans.Item.ItemStack;

public class PlayerData {

    public PlayerEntity playerEntity;
    public ItemStack heldStack = ItemStack.emptyStack();
    public Container openContainer;
    public Inventory playerInventory;

    public PlayerData(PlayerEntity playerEntity) {
        this.playerEntity = playerEntity;
        playerInventory = playerEntity.inventory;
        openContainer = new InventoryContainer(playerInventory);
    }


    public void swapStack(short slot) {
        Slot itemSlot = openContainer.slots.get(slot);
        if(itemSlot != null) {
            if(itemSlot.canItemBeAdded(heldStack)) {
               heldStack = itemSlot.swapItemStacks(heldStack);
            }
        }
    }

    public void splitStack(short slot) {
        if(heldStack.isEmpty()) {
            ItemStack itemStack = openContainer.slots.get(slot).getContents();
            if (!itemStack.isEmpty()) {
                heldStack = itemStack.splitStack();
            }
        }
    }

    public void putOne(short slot) {
        if(!heldStack.isEmpty()) {
            if(openContainer.slots.get(slot).canItemBeAdded(heldStack) && openContainer.slots.get(slot).getContents().canAdd(1,heldStack.item)) {
                heldStack.count -= 1;
            }
        }
    }

}
