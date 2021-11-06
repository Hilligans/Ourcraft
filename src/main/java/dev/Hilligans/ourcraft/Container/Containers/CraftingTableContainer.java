package dev.Hilligans.ourcraft.Container.Containers;

import dev.Hilligans.ourcraft.Client.Client;
import dev.Hilligans.ourcraft.Client.Rendering.ContainerScreen;
import dev.Hilligans.ourcraft.Container.Container;
import dev.Hilligans.ourcraft.Container.CraftingContainer;
import dev.Hilligans.ourcraft.Data.Other.IInventory;

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
