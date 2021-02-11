package Hilligans.Container;

import Hilligans.Client.MatrixStack;
import Hilligans.Client.Rendering.ContainerScreen;
import Hilligans.ClientMain;
import Hilligans.Container.Containers.ChestContainer;
import Hilligans.Container.Containers.InventoryContainer;
import Hilligans.Data.Other.IInventory;
import Hilligans.Server.IInventoryChanged;
import Hilligans.Util.Settings;
import io.netty.channel.ChannelId;

import java.util.ArrayList;

public abstract class Container {

    public int type;

    public int textureX = 0;
    public int textureY = 0;

    public int channelId;

    public int uniqueId = -1;

    public Container(int type) {
        this.type = type;
    }

    //TODO slots should be added with an x based off their texture, the x and y pos need to be able to be recalculated when the screen is resized

    public ArrayList<Slot> slots = new ArrayList<>();

    public Slot getSlot(int slot) {
        if(slot < slots.size()) {
            return slots.get(slot);
        }
        return null;
    }

    public Container setPlayerId(int channelId) {
        this.channelId = channelId;
        return this;
    }

    public Container setUniqueId(int id) {
        this.uniqueId = id;
        return this;
    }

    public void addSlot(Slot slot) {
        slot.setContainerAndId((short) slots.size(),this);
        //slot.id = (short) slots.size();
        //slot.container = this;
        slots.add(slot);
    }

    public void render(MatrixStack matrixStack) {
        for(Slot slot : slots) {
            slot.render(matrixStack);
        }
    }

    public void setTextureSize(int x, int y) {
        this.textureX = x;
        this.textureY = y;
    }

    public void resize() {
        int newX = (int)(ClientMain.windowX / 2 - textureX * Settings.guiSize / 2);
        int newY = (int)(ClientMain.windowY / 2 - textureY * Settings.guiSize / 2);
        for(Slot slot : slots) {
            slot.x = (int)(newX + slot.startX * Settings.guiSize);
            slot.y = (int)(newY + slot.startY * Settings.guiSize);
        }
    }


    public abstract ContainerScreen<?> getContainerScreen();

    public void closeContainer() {
        for(Slot slot : slots) {
            slot.onClose();
        }
    }

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
        containers.add(ChestContainer::new);
    }

    static int id = 0;

    public static int getId() {
        return id++;
    }

}