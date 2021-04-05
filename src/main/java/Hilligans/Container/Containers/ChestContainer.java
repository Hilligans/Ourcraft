package Hilligans.Container.Containers;

import Hilligans.Client.ClientPlayerData;
import Hilligans.Client.Rendering.ContainerScreen;
import Hilligans.Client.Rendering.Screens.ContainerScreens.ChestScreen;
import Hilligans.Container.Container;
import Hilligans.Container.Slot;
import Hilligans.Data.Other.Inventory;
import Hilligans.Data.Other.JoinedInventory;

public class ChestContainer extends Container {

    public ChestContainer() {
        this(new Inventory(27), ClientPlayerData.inventory);
    }

    public ChestContainer(Inventory inventory, Inventory playerInventory) {
        super(1,new JoinedInventory(inventory,playerInventory));
        setTextureSize(158,162);
        for(int y = 0; y < 3; y++) {
            for (int x = 0; x < 9; x++) {
                addSlot(new Slot(7 + x * 16, 12 + y * 16, inventory, x + y * 9));
            }
        }
        for(int x = 0; x < 9; x++) {
            addSlot(new Slot(7 + 16 * x,139, playerInventory,x));
        }
        for(int y = 0; y < 4; y++) {
            for (int x = 0; x < 9; x++) {
                addSlot(new Slot(7 + x * 16, 70 + y * 16,playerInventory,9 + x + y * 9));
            }
        }
        resize();
    }

    @Override
    public ContainerScreen<?> getContainerScreen() {
        return new ChestScreen();
    }
}
