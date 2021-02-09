package Hilligans.Server;

import Hilligans.Container.Container;
import Hilligans.Container.Containers.InventoryContainer;
import Hilligans.Container.Slot;
import Hilligans.Data.Other.Inventory;
import Hilligans.Entity.Entities.ItemEntity;
import Hilligans.Entity.Entity;
import Hilligans.Entity.LivingEntities.PlayerEntity;
import Hilligans.Item.ItemStack;
import Hilligans.ServerMain;

public class PlayerData {

    public PlayerEntity playerEntity;
    public ItemStack heldStack = ItemStack.emptyStack();
    public Container openContainer;
    public Inventory playerInventory;
    public boolean isCreative = false;

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

    public void dropItem(short slot) {
        if(slot == -1) {
            if(!heldStack.isEmpty()) {
                ServerMain.world.addEntity(new ItemEntity(playerEntity.x, playerEntity.y, playerEntity.z, Entity.getNewId(), heldStack).setVel(playerEntity.getForeWard().mul(-0.5f).add(0,0.25f,0)));
                heldStack = ItemStack.emptyStack();
            }
        } else {
            Slot itemSlot = openContainer.getSlot(slot);
            if(itemSlot != null) {
                if(!itemSlot.getContents().isEmpty()) {
                    ServerMain.world.addEntity(new ItemEntity(playerEntity.x, playerEntity.y, playerEntity.z, Entity.getNewId(), itemSlot.getContents()).setVel(playerEntity.getForeWard().mul(-0.5f).add(0,0.25f,0)));
                    itemSlot.setContents(ItemStack.emptyStack());
                }
            }
        }
    }

}
