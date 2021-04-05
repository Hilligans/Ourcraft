package Hilligans.Container.Containers;

import Hilligans.Client.ClientPlayerData;
import Hilligans.Client.Rendering.ContainerScreen;
import Hilligans.Client.Rendering.Screens.ContainerScreens.CreativeInventoryScreen;
import Hilligans.Container.Container;
import Hilligans.Container.Slot;
import Hilligans.Data.Other.IInventory;
import Hilligans.Data.Other.Inventory;
import Hilligans.Data.Other.JoinedInventory;
import Hilligans.Data.Other.ServerSidedData;
import Hilligans.Item.BlockItem;
import Hilligans.Item.Item;
import Hilligans.Item.ItemStack;
import Hilligans.Item.Items;

public class CreativeContainer extends Container {

    public CreativeContainer() {
        this(ClientPlayerData.inventory,new Inventory(Math.max(Items.ITEMS.size() + ServerSidedData.getInstance().ITEMS.size(),54)));
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

    public static IInventory createInventory() {
        Inventory inventory = new Inventory(Math.max(Items.ITEMS.size() + ServerSidedData.getInstance().ITEMS.size(),54));
        int x = 0;
        for(Item item : Items.ITEMS) {
            if(item instanceof BlockItem && !((BlockItem) item).block.blockProperties.airBlock) {
                inventory.setItem(x, new ItemStack(item, (byte) 1));
                x++;
            }
        }
        for(Item item : ServerSidedData.getInstance().ITEMS) {
            if(item instanceof BlockItem && !((BlockItem) item).block.blockProperties.airBlock) {
                inventory.setItem(x, new ItemStack(item, (byte) 1));
                x++;
            }
        }
        return inventory;
    }





}
