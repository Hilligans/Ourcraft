package dev.hilligans.ourcraft.container.containers;

import dev.hilligans.ourcraft.client.Client;
import dev.hilligans.ourcraft.client.rendering.ContainerScreen;
import dev.hilligans.ourcraft.container.Container;
import dev.hilligans.ourcraft.container.CraftingContainer;
import dev.hilligans.ourcraft.data.other.IInventory;

public class CraftingTableContainer extends Container implements CraftingContainer {
    public CraftingTableContainer(int type, IInventory inventory) {
        super(type, inventory);
    }

    @Override
    public ContainerScreen<?> getContainerScreen(Client client) {
        return null;
    }

    @Override
    public int getStartingIndex() {
        return 0;
    }

    @Override
    public int getCraftingWidth() {
        return 3;
    }

    @Override
    public int getCraftingHeight() {
        return 3;
    }
}
