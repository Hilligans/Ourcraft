package dev.Hilligans.ourcraft.Container.Containers;

import dev.Hilligans.ourcraft.Client.Client;
import dev.Hilligans.ourcraft.Client.Rendering.ContainerScreen;
import dev.Hilligans.ourcraft.Client.Rendering.Screens.ContainerScreens.InventoryScreen;
import dev.Hilligans.ourcraft.ClientMain;
import dev.Hilligans.ourcraft.Container.Container;
import dev.Hilligans.ourcraft.Container.Slot;
import dev.Hilligans.ourcraft.Data.Other.Inventory;

public class InventoryContainer extends Container {

    public InventoryContainer() {
        this(ClientMain.getClient().playerData.inventory);
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
    public ContainerScreen<?> getContainerScreen(Client client) {
        return new InventoryScreen(client);
    }

}
