package Hilligans.Container.Containers;

import Hilligans.Client.ClientData;
import Hilligans.Client.Rendering.ContainerScreen;
import Hilligans.Client.Rendering.Screens.ContainerScreens.ChestScreen;
import Hilligans.ClientMain;
import Hilligans.Container.Container;
import Hilligans.Container.Slot;
import Hilligans.Data.Other.IInventory;
import Hilligans.Data.Other.Inventory;
import Hilligans.Network.Packet.Server.SUpdateContainer;
import Hilligans.Network.ServerNetworkHandler;
import Hilligans.Util.Settings;

public class ChestContainer extends Container {



    public ChestContainer() {
        super(1);
        Inventory chestInventory = new Inventory(27);

        //34
        int slotSize = (int) (16 * Settings.guiSize);
        int startY = (int) (ClientMain.windowY / 2 - 34 * Settings.guiSize / 2 - 4 * slotSize);
        int startX = (int) (ClientMain.windowX / 2 - 4.5f * slotSize);

        for(int y = 0; y < 3; y++) {
            for (int x = 0; x < 9; x++) {
                addSlot(new Slot(startX + x * slotSize, (int) (startY + 12 * Settings.guiSize + y * slotSize),chestInventory,x + y * 9));
            }
        }

        for(int x = 0; x < 9; x++) {
            addSlot(new Slot(startX + x * slotSize, (int) (startY + 27 * Settings.guiSize + slotSize * 7), ClientData.inventory,x));
        }

        for(int y = 0; y < 4; y++) {
            for (int x = 0; x < 9; x++) {
                addSlot(new Slot(startX + x * slotSize, (int) (startY + y * slotSize + 22 * Settings.guiSize + slotSize * 3),ClientData.inventory,9 + x + y * 9));
            }
        }
    }

    public ChestContainer(Inventory inventory, Inventory playerInventory) {
        super(1);
        for(int x = 0; x < 27; x++) {
            addSlot(new Slot(0, 0, inventory,x));
        }

        for(int x = 0; x < 45; x++) {
            addSlot(new Slot(0, 0, playerInventory,x));
        }
    }

    @Override
    public ContainerScreen<?> getContainerScreen() {
        return new ChestScreen();
    }
}
