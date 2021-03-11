package Hilligans.Container.Containers;

import Hilligans.Client.Rendering.ContainerScreen;
import Hilligans.Client.Rendering.Screens.CustomContainerScreen;
import Hilligans.Container.Container;
import Hilligans.Container.ContainerFetcher;
import Hilligans.Container.Slot;
import Hilligans.Data.Other.Inventory;

import java.util.ArrayList;

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
            container.addSlot(slot.copy());
            slot.inventory = inventory;
        }
        return container;
    }
}
