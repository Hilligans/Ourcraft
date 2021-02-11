package Hilligans.Container.Containers;

import Hilligans.Client.ClientData;
import Hilligans.Client.Rendering.ContainerScreen;
import Hilligans.Client.Rendering.Screens.ContainerScreens.InventoryScreen;
import Hilligans.ClientMain;
import Hilligans.Container.Container;
import Hilligans.Container.Slot;
import Hilligans.Data.Other.IInventory;
import Hilligans.Data.Other.Inventory;
import Hilligans.Network.Packet.Server.SUpdateContainer;
import Hilligans.Network.ServerNetworkHandler;
import Hilligans.Util.Settings;

public class InventoryContainer extends Container {

    public InventoryContainer() {
        this(ClientData.inventory);
    }

    public InventoryContainer(Inventory inventory) {
        super(0);
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
