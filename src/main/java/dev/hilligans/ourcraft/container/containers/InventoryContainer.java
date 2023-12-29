package dev.hilligans.ourcraft.container.containers;

import dev.hilligans.ourcraft.client.Client;
import dev.hilligans.ourcraft.client.rendering.ContainerScreen;
import dev.hilligans.ourcraft.client.rendering.screens.container.screens.InventoryScreen;
import dev.hilligans.ourcraft.ClientMain;
import dev.hilligans.ourcraft.container.Container;
import dev.hilligans.ourcraft.container.Slot;
import dev.hilligans.ourcraft.data.other.Inventory;

public class InventoryContainer extends Container {

    public InventoryContainer(Client client) {
        this(client.playerData.inventory);
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
