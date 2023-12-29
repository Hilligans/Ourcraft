package dev.hilligans.ourcraft.container.containers;

import dev.hilligans.ourcraft.GameInstance;
import dev.hilligans.ourcraft.client.Client;
import dev.hilligans.ourcraft.client.rendering.ContainerScreen;
import dev.hilligans.ourcraft.client.rendering.screens.container.screens.CreativeInventoryScreen;
import dev.hilligans.ourcraft.ClientMain;
import dev.hilligans.ourcraft.container.Container;
import dev.hilligans.ourcraft.container.Slot;
import dev.hilligans.ourcraft.data.other.IInventory;
import dev.hilligans.ourcraft.data.other.Inventory;
import dev.hilligans.ourcraft.data.other.JoinedInventory;
import dev.hilligans.ourcraft.item.BlockItem;
import dev.hilligans.ourcraft.item.Item;
import dev.hilligans.ourcraft.item.ItemStack;
import dev.hilligans.ourcraft.Ourcraft;

public class CreativeContainer extends Container {

    public CreativeContainer(Client client) {
        this(client.playerData.inventory,new Inventory(Math.max(client.getGameInstance().ITEMS.ELEMENTS.size(),54)));
    }

    public CreativeContainer(IInventory playerInventory, IInventory creativeInventory) {
        super(2, new JoinedInventory(playerInventory,creativeInventory));
        JoinedInventory joinedInventory = new JoinedInventory(playerInventory,creativeInventory);
        setTextureSize(158,210);
        addPlayerInventorySlots(7,118,joinedInventory,0);
        for(int y = 0; y < 6; y++) {
            for(int x = 0; x < 9; x++) {
                addSlot(new Slot(7 + x * 16,12 + y * 16,joinedInventory,playerInventory.getSize() + x + y * 9));
            }
        }
        resize();
    }

    @Override
    public ItemStack swapStack(short slot, ItemStack heldStack) {
        if(slot < 45) {
            return super.swapStack(slot,heldStack);
        }
        Slot itemSlot = getSlot(slot);
        if(itemSlot != null) {
            ItemStack slotStack = itemSlot.getContents();
            if(slotStack.isEmpty()) {
                return ItemStack.emptyStack();
            }
            if(heldStack.isEmpty()) {
                return slotStack;
            } else {
                if(heldStack.item == slotStack.item) {
                    heldStack.add(1);
                    return heldStack;
                }
            }
        }
        return ItemStack.emptyStack();
    }

    @Override
    public ContainerScreen<?> getContainerScreen() {
        return new CreativeInventoryScreen();
    }

    public static IInventory createInventory(GameInstance gameInstance) {
        Inventory inventory = new Inventory(Math.max(gameInstance.ITEMS.ELEMENTS.size() ,54));
        int x = 0;
        for(Item item : gameInstance.ITEMS.ELEMENTS) {
            if(!(item instanceof BlockItem) || !((BlockItem) item).block.blockProperties.airBlock) {
                inventory.setItem(x, new ItemStack(item, (byte) 1));
                x++;
            }
        }
        return inventory;
    }





}
