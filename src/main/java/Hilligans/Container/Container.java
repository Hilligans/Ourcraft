package Hilligans.Container;

import Hilligans.Client.MatrixStack;
import Hilligans.Client.Rendering.ContainerScreen;
import Hilligans.Container.Containers.InventoryContainer;

import java.util.ArrayList;

public abstract class Container {

    public int type;

    public Container(int type) {
        this.type = type;
    }

    public ArrayList<Slot> slots = new ArrayList<>();

    public void render(MatrixStack matrixStack) {
        //System.out.println("RENDERING");
        for(Slot slot : slots) {
            slot.render(matrixStack);
        }
    }

    public abstract ContainerScreen<?> getContainerScreen();

    public static ArrayList<ContainerFetcher> containers = new ArrayList<>();

    public static void register() {
        containers.add(InventoryContainer::new);
    }

}
