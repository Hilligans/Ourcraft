package dev.hilligans.ourcraft.Container.Containers;

import dev.hilligans.ourcraft.Client.Client;
import dev.hilligans.ourcraft.Client.Rendering.ContainerScreen;
import dev.hilligans.ourcraft.Container.Container;
import dev.hilligans.ourcraft.Container.CraftingContainer;
import dev.hilligans.ourcraft.Data.Other.IInventory;

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
