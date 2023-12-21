package dev.hilligans.ourcraft.client.rendering;

import dev.hilligans.ourcraft.client.Client;
import dev.hilligans.ourcraft.client.MatrixStack;
import dev.hilligans.ourcraft.client.rendering.graphics.RenderWindow;
import dev.hilligans.ourcraft.client.rendering.graphics.api.GraphicsContext;
import dev.hilligans.ourcraft.container.Container;
import dev.hilligans.ourcraft.container.Slot;
import dev.hilligans.ourcraft.item.ItemStack;
import dev.hilligans.ourcraft.network.packet.client.CModifyStack;
import dev.hilligans.ourcraft.Ourcraft;
import dev.hilligans.ourcraft.recipe.helper.RecipeHelper;

import static org.lwjgl.glfw.GLFW.*;

public abstract class ContainerScreen<T extends Container> extends ScreenBase {

    public T container;

    public void setContainer(Container container) {
        this.container = (T) container;
    }

    public abstract T getContainer();


    public ContainerScreen() {
    }

    public RecipeHelper recipeHelper = new RecipeHelper(Ourcraft.GAME_INSTANCE);

    @Override
    public void render(RenderWindow window, MatrixStack matrixStack, GraphicsContext graphicsContext) {
        super.render(window, matrixStack, graphicsContext);
        container.render(window, matrixStack);

    }

    @Override
    public void mouseClick(int x, int y, int mouseButton) {
        super.mouseClick(x, y, mouseButton);

        Slot slot = container.getSlotAt(x,y);
        Client client = getClient();
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
