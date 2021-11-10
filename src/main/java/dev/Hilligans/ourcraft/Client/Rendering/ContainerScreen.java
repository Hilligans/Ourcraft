package dev.Hilligans.ourcraft.Client.Rendering;

import dev.Hilligans.ourcraft.Client.Client;
import dev.Hilligans.ourcraft.Client.MatrixStack;
import dev.Hilligans.ourcraft.Container.Container;
import dev.Hilligans.ourcraft.Container.Slot;
import dev.Hilligans.ourcraft.Item.ItemStack;
import dev.Hilligans.ourcraft.Network.Packet.Client.CModifyStack;
import dev.Hilligans.ourcraft.Ourcraft;
import dev.Hilligans.ourcraft.Recipe.RecipeHelper.RecipeHelper;

import static org.lwjgl.glfw.GLFW.*;

public abstract class ContainerScreen<T extends Container> extends ScreenBase {

    public T container;

    public void setContainer(Container container) {
        this.container = (T) container;
    }

    public abstract T getContainer();

    public ContainerScreen(Client client) {
        super(client);
    }

    public RecipeHelper recipeHelper = new RecipeHelper(Ourcraft.GAME_INSTANCE);

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
                ItemStack oldStack = client.playerData.heldStack.copy();
                client.playerData.heldStack = container.swapStack(slot.id, client.playerData.heldStack);
                if(!client.playerData.heldStack.equals(oldStack)) {
                    client.sendPacket(new CModifyStack(slot.id, (byte) 0));
                }
            } else if(mouseButton == GLFW_MOUSE_BUTTON_2) {
                boolean empty = client.playerData.heldStack.isEmpty();
                client.playerData.heldStack = container.splitStack(slot.id, client.playerData.heldStack);
                if (empty && !client.playerData.heldStack.isEmpty()) {
                    client.sendPacket(new CModifyStack(slot.id, (byte) 1));
                }

            } else if(mouseButton == GLFW_MOUSE_BUTTON_MIDDLE && client.playerData.creative) {
                ItemStack stack = container.copyStack(slot.id,client.playerData.heldStack);
                if(client.playerData.heldStack != stack) {
                    client.playerData.heldStack = stack;
                    client.sendPacket(new CModifyStack(slot.id,(byte)3));
                }
            } else {
                if(container.putOne(slot.id, client.playerData.heldStack)) {
                    client.sendPacket(new CModifyStack(slot.id, (byte) 2));
                }
            }
        }
    }

    public int getWidth() {
        return 0;
    }

    public int getHeight() {
        return 0;
    }
}
