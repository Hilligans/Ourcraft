package dev.Hilligans.Ourcraft.Container.Containers;

import dev.Hilligans.Ourcraft.Client.Client;
import dev.Hilligans.Ourcraft.Client.Rendering.ContainerScreen;
import dev.Hilligans.Ourcraft.Client.Rendering.Screens.ContainerScreens.InventoryScreen;
import dev.Hilligans.Ourcraft.ClientMain;
import dev.Hilligans.Ourcraft.Container.Container;
import dev.Hilligans.Ourcraft.Container.Slot;
import dev.Hilligans.Ourcraft.Data.Other.Inventory;

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
