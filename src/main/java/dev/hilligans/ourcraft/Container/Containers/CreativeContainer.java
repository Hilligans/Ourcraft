package dev.hilligans.ourcraft.Container.Containers;

import dev.hilligans.ourcraft.Client.Client;
import dev.hilligans.ourcraft.Client.Rendering.ContainerScreen;
import dev.hilligans.ourcraft.Client.Rendering.Screens.ContainerScreens.CreativeInventoryScreen;
import dev.hilligans.ourcraft.ClientMain;
import dev.hilligans.ourcraft.Container.Container;
import dev.hilligans.ourcraft.Container.Slot;
import dev.hilligans.ourcraft.Data.Other.IInventory;
import dev.hilligans.ourcraft.Data.Other.Inventory;
import dev.hilligans.ourcraft.Data.Other.JoinedInventory;
import dev.hilligans.ourcraft.Item.BlockItem;
import dev.hilligans.ourcraft.Item.Item;
import dev.hilligans.ourcraft.Item.ItemStack;
import dev.hilligans.ourcraft.Ourcraft;

public class CreativeContainer extends Container {

    public CreativeContainer() {
        this(ClientMain.getClient().playerData.inventory,new Inventory(Math.max(Ourcraft.GAME_INSTANCE.ITEMS.ELEMENTS.size(),54)));
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
    public ContainerScreen<?> getContainerScreen(Client client) {
        return new CreativeInventoryScreen(client);
    }

    public static IInventory createInventory() {
        Inventory inventory = new Inventory(Math.max(Ourcraft.GAME_INSTANCE.ITEMS.ELEMENTS.size() ,54));
        int x = 0;
        for(Item item : Ourcraft.GAME_INSTANCE.ITEMS.ELEMENTS) {
            if(!(item instanceof BlockItem) || !((BlockItem) item).block.blockProperties.airBlock) {
                inventory.setItem(x, new ItemStack(item, (byte) 1));
                x++;
            }
        }
        return inventory;
    }





}
