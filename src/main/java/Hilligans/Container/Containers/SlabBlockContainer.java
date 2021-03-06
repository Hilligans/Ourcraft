package Hilligans.Container.Containers;

import Hilligans.Client.ClientPlayerData;
import Hilligans.Client.Rendering.ContainerScreen;
import Hilligans.ClientMain;
import Hilligans.Container.Container;
import Hilligans.Container.Slot;
import Hilligans.Data.Other.Inventory;
import Hilligans.Data.Other.JoinedInventory;

public class SlabBlockContainer extends Container {

    public SlabBlockContainer() {
        this(new Inventory(3), ClientMain.getClient().playerData.inventory);
    }

    public SlabBlockContainer(Inventory inventory, Inventory playerInventory) {
        super(3,new JoinedInventory(inventory,playerInventory));
        addSlot(new Slot(55, 47, inventory, 0));
        addSlot(new Slot(71, 47, inventory, 1));
        addSlot(new Slot(87, 47, inventory, 2));

        setTextureSize(158,162);

        for(int x = 0; x < 9; x++) {
            addSlot(new Slot(7 + 16 * x,139, playerInventory,x));
        }
        for(int y = 0; y < 4; y++) {
            for (int x = 0; x < 9; x++) {
                addSlot(new Slot(7 + x * 16, 70 + y * 16,playerInventory,9 + x + y * 9));
            }
        }
    }

    @Override
    public ContainerScreen<?> getContainerScreen() {
        return null;
    }
}
