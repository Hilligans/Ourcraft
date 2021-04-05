package Hilligans.Client.Rendering;

import Hilligans.Client.ClientPlayerData;
import Hilligans.Client.MatrixStack;
import Hilligans.Container.Container;
import Hilligans.Container.Slot;
import Hilligans.Item.ItemStack;
import Hilligans.Network.ClientNetworkHandler;
import Hilligans.Network.Packet.Client.CModifyStack;

import static org.lwjgl.glfw.GLFW.*;

public abstract class ContainerScreen<T extends Container> extends ScreenBase {

    public T container;

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

        Slot slot = container.getSlotAt(x,y);
        if(slot != null) {
            if(mouseButton == GLFW_MOUSE_BUTTON_1) {
                ItemStack oldStack = ClientPlayerData.heldStack.copy();
                ClientPlayerData.heldStack = container.swapStack(slot.id, ClientPlayerData.heldStack);
                if(!ClientPlayerData.heldStack.equals(oldStack)) {
                    ClientNetworkHandler.sendPacketDirect(new CModifyStack(slot.id, (byte) 0));
                }
            } else if(mouseButton == GLFW_MOUSE_BUTTON_2) {
                boolean empty = ClientPlayerData.heldStack.isEmpty();
                ClientPlayerData.heldStack = container.splitStack(slot.id, ClientPlayerData.heldStack);
                if (empty && !ClientPlayerData.heldStack.isEmpty()) {
                    ClientNetworkHandler.sendPacketDirect(new CModifyStack(slot.id, (byte) 1));
                }

            } else if(mouseButton == GLFW_MOUSE_BUTTON_MIDDLE && ClientPlayerData.creative) {
                ItemStack stack = container.copyStack(slot.id,ClientPlayerData.heldStack);
                if(ClientPlayerData.heldStack != stack) {
                    ClientPlayerData.heldStack = stack;
                    ClientNetworkHandler.sendPacketDirect(new CModifyStack(slot.id,(byte)3));
                }
            } else {
                if(container.putOne(slot.id, ClientPlayerData.heldStack)) {
                    ClientNetworkHandler.sendPacketDirect(new CModifyStack(slot.id, (byte) 2));
                }
            }
        }
    }
}
