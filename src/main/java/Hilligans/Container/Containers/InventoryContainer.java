package Hilligans.Container.Containers;

import Hilligans.Client.ClientPlayerData;
import Hilligans.Client.Rendering.ContainerScreen;
import Hilligans.Client.Rendering.Screens.ContainerScreens.InventoryScreen;
import Hilligans.Container.Container;
import Hilligans.Container.Slot;
import Hilligans.Data.Other.Inventory;

public class InventoryContainer extends Container {

    public InventoryContainer() {
        this(ClientPlayerData.inventory);
    }

    public InventoryContainer(Inventory inventory) {
        super(0,inventory);
        setTextureSize(158,99);
        for(int y = 0; y < 4; y++) {
            for (int x = 0; x < 9; x++) {
                addSlot(new Slot(7 + x * 16,7 + y * 16,inventory,9 + x + y * 9));
            }
        }
        for(int x = 0; x < 9; x++) {
            addSlot(new Slot(7 + x * 16,76,inventory,x));
        }
        resize();
    }

    @Override
    public ContainerScreen<?> getContainerScreen() {
        return new InventoryScreen();
    }

}
