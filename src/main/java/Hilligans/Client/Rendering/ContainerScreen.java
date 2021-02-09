package Hilligans.Client.Rendering;

import Hilligans.Client.ClientData;
import Hilligans.Client.MatrixStack;
import Hilligans.Container.Container;
import Hilligans.Container.Slot;
import Hilligans.Network.ClientNetworkHandler;
import Hilligans.Network.Packet.Client.CModifyStack;
import Hilligans.Util.Settings;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_1;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_2;

public abstract class ContainerScreen<T extends Container> extends ScreenBase {

    T container;

    public void setContainer(Container container) {
        this.container = (T) container;
    }

    public abstract T getContainer();

    @Override
    public void render(MatrixStack matrixStack) {
        super.render(matrixStack);
        container.render(matrixStack);
    }

    @Override
    public void mouseClick(int x, int y, int mouseButton) {
        super.mouseClick(x, y, mouseButton);
        byte z = 0;
        for(Slot slot : container.slots) {
            if(slot.x < x && slot.y < y && slot.x + 16 * Settings.guiSize > x && slot.y + 16 * Settings.guiSize > y) {
                if(mouseButton == GLFW_MOUSE_BUTTON_1) {
                    if (slot.canItemBeAdded(ClientData.heldStack)) {
                        ClientData.heldStack = slot.swapItemStacks(ClientData.heldStack);
                        ClientNetworkHandler.sendPacket(new CModifyStack(z, (byte) 0));
                    }
                } else if(mouseButton == GLFW_MOUSE_BUTTON_2) {
                    if(ClientData.heldStack.isEmpty()) {
                        ClientData.heldStack = slot.getContents().splitStack();
                        if(!ClientData.heldStack.isEmpty()) {
                            ClientNetworkHandler.sendPacket(new CModifyStack(z, (byte) 1));
                        }
                    } else {
                        if(slot.getContents().canAdd(1,ClientData.heldStack.item)) {
                            ClientData.heldStack.count -= 1;
                            ClientNetworkHandler.sendPacket(new CModifyStack(z, (byte) 2));
                        }
                    }
                }
                break;
            }
            z++;
        }
    }
}
