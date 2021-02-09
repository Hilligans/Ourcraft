package Hilligans.Container.Containers;

import Hilligans.Client.ClientData;
import Hilligans.Client.Rendering.ContainerScreen;
import Hilligans.Client.Rendering.Screens.InventoryScreen;
import Hilligans.ClientMain;
import Hilligans.Container.Container;
import Hilligans.Container.Slot;
import Hilligans.Data.Other.Inventory;
import Hilligans.Item.ItemStack;
import Hilligans.Util.Settings;

public class InventoryContainer extends Container {

    public InventoryContainer() {
        super(0);
        int startX = ClientMain.windowX / 2 - 316;
        int startY = ClientMain.windowY / 2 - 198;


        for(int y = 0; y < 4; y++) {
            for (int x = 0; x < 9; x++) {
                addSlot(new Slot((int)(startX + x * 64 + 7 * Settings.guiSize), (int)(startY + y * 64 + 7 * Settings.guiSize),ClientData.inventory,9 + x + y * 9));
            }
        }

        for(int x = 0; x < 9; x++) {
            addSlot(new Slot((int)(startX + x * 64 + 7 * Settings.guiSize), (int)(startY + 256 + 12 * Settings.guiSize),ClientData.inventory,x));
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
