package Hilligans.Container;

import Hilligans.Client.MatrixStack;
import Hilligans.Client.Rendering.ContainerScreen;
import Hilligans.Container.Containers.InventoryContainer;
import Hilligans.Util.Settings;

import java.util.ArrayList;

public abstract class Container {

    public int type;

    public Container(int type) {
        this.type = type;
    }

    public ArrayList<Slot> slots = new ArrayList<>();

    public Slot getSlot(int slot) {
        if(slot < slots.size()) {
            return slots.get(slot);
        }
        return null;
    }

    public void addSlot(Slot slot) {
        slot.id = (short) slots.size();
        slots.add(slot);
    }

    public void render(MatrixStack matrixStack) {
        //System.out.println("RENDERING");
        for(Slot slot : slots) {
            slot.render(matrixStack);
        }
    }

    public abstract ContainerScreen<?> getContainerScreen();

    public Slot getSlotAt(int x, int y) {
        for(Slot slot : slots) {
            if(slot.x < x && slot.y < y && slot.x + 16 * Settings.guiSize > x && slot.y + 16 * Settings.guiSize > y) {
                return slot;
            }
        }
        return null;
    }

    public static ArrayList<ContainerFetcher> containers = new ArrayList<>();

    public static void register() {
        containers.add(InventoryContainer::new);
    }

}
