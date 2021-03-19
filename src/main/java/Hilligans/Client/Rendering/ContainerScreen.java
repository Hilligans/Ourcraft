package Hilligans.Client.Rendering;

import Hilligans.Client.ClientData;
import Hilligans.Client.MatrixStack;
import Hilligans.Container.Container;
import Hilligans.Container.Slot;
import Hilligans.Item.ItemStack;
import Hilligans.Network.ClientNetworkHandler;
import Hilligans.Network.Packet.Client.CModifyStack;
import Hilligans.Util.Settings;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_1;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_2;

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
                ItemStack oldStack = ClientData.heldStack.copy();
                ClientData.heldStack = container.swapStack(slot.id,ClientData.heldStack);
                if(!ClientData.heldStack.equals(oldStack)) {
                    ClientNetworkHandler.sendPacket(new CModifyStack(slot.id, (byte) 0));
                }
            } else if(mouseButton == GLFW_MOUSE_BUTTON_2) {
                boolean empty = ClientData.heldStack.isEmpty();
                ClientData.heldStack = container.splitStack(slot.id,ClientData.heldStack);
                if(empty && !ClientData.heldStack.isEmpty()) {
                    ClientNetworkHandler.sendPacket(new CModifyStack(slot.id, (byte) 1));
                }
            } else {
                if(container.putOne(slot.id,ClientData.heldStack)) {
                    ClientNetworkHandler.sendPacket(new CModifyStack(slot.id, (byte) 2));
                }
            }
        }
    }
}
