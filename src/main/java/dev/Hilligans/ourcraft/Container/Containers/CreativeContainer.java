package dev.Hilligans.ourcraft.Container.Containers;

import dev.Hilligans.ourcraft.Client.Client;
import dev.Hilligans.ourcraft.Client.Rendering.ContainerScreen;
import dev.Hilligans.ourcraft.Client.Rendering.Screens.ContainerScreens.CreativeInventoryScreen;
import dev.Hilligans.ourcraft.ClientMain;
import dev.Hilligans.ourcraft.Container.Container;
import dev.Hilligans.ourcraft.Container.Slot;
import dev.Hilligans.ourcraft.Data.Other.IInventory;
import dev.Hilligans.ourcraft.Data.Other.Inventory;
import dev.Hilligans.ourcraft.Data.Other.JoinedInventory;
import dev.Hilligans.ourcraft.Item.BlockItem;
import dev.Hilligans.ourcraft.Item.Item;
import dev.Hilligans.ourcraft.Item.ItemStack;
import dev.Hilligans.ourcraft.Item.Items;

public class CreativeContainer extends Container {

    public CreativeContainer() {
        this(ClientMain.getClient().playerData.inventory,new Inventory(Math.max(Items.ITEMS.size(),54)));
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
        Inventory inventory = new Inventory(Math.max(Items.ITEMS.size() ,54));
        int x = 0;
        for(Item item : Items.ITEMS) {
            if(!(item instanceof BlockItem) || !((BlockItem) item).block.blockProperties.airBlock) {
                inventory.setItem(x, new ItemStack(item, (byte) 1));
                x++;
            }
        }
        return inventory;
    }





}
