package Hilligans.Container.Containers;

import Hilligans.Client.ClientData;
import Hilligans.Client.Rendering.ContainerScreen;
import Hilligans.Client.Rendering.Screens.ContainerScreens.InventoryScreen;
import Hilligans.ClientMain;
import Hilligans.Container.Container;
import Hilligans.Container.Slot;
import Hilligans.Data.Other.Inventory;
import Hilligans.Util.Settings;

public class InventoryContainer extends Container {

    public InventoryContainer() {
        super(0);
        int slotSize = (int) (16 * Settings.guiSize);
        int startX = (int) (ClientMain.windowX / 2 - 4.5f * slotSize);
        int startY = (int) (ClientMain.windowY / 2 - 2.5 * slotSize - Settings.guiSize * 2.5);
        for(int y = 0; y < 4; y++) {
            for (int x = 0; x < 9; x++) {
                addSlot(new Slot(startX + x * slotSize, startY + y * slotSize,ClientData.inventory,9 + x + y * 9));
            }
        }
        for(int x = 0; x < 9; x++) {
            addSlot(new Slot(startX + x * slotSize, (int)(startY + slotSize * 4 + 5 * Settings.guiSize),ClientData.inventory,x));
        }
    }

    public InventoryContainer(Inventory inventory) {
        super(0);
        for(int y = 0; y < 4; y++) {
            for (int x = 0; x < 9; x++) {
                addSlot(new Slot(0,0,inventory,9 + x + y * 9));
            }
        }
        for(int x = 0; x < 9; x++) {
            addSlot(new Slot(0,0,inventory,x));
        }
    }

    @Override
    public ContainerScreen<?> getContainerScreen() {
        return new InventoryScreen();
    }
}
