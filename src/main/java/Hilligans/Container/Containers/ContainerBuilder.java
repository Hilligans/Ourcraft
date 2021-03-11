package Hilligans.Container.Containers;

import Hilligans.Client.Rendering.ContainerScreen;
import Hilligans.Client.Rendering.Screens.ContainerScreens.CustomContainerScreen;
import Hilligans.Container.Container;
import Hilligans.Container.ContainerFetcher;
import Hilligans.Container.Slot;
import Hilligans.Data.Other.Inventory;

public class ContainerBuilder implements ContainerFetcher {

    short id;
    Slot[] slots;
    String textureName;

    public ContainerBuilder(short id, String textureName, Slot[] slots) {
        this.id = id;
        this.slots = slots;
        this.textureName = textureName;
    }


    @Override
    public Container getContainer() {
        Inventory inventory = new Inventory(slots.length);
        Container container = new Container(id) {
            @Override
            public ContainerScreen<?> getContainerScreen() {
                return new CustomContainerScreen(textureName);
            }
        };
        for(Slot slot : slots) {
            slot.inventory = inventory;
            container.addSlot(slot.copy());
        }
        container.setTextureSize(158,162);
        container.resize();
        return container;
    }
}
