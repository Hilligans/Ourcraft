package dev.Hilligans.Client.Mouse;

import dev.Hilligans.Block.Blocks;
import dev.Hilligans.Client.Camera;
import dev.Hilligans.Client.Client;
import dev.Hilligans.Data.Other.BlockPos;
import dev.Hilligans.Data.Other.BlockStates.BlockState;
import dev.Hilligans.Item.ItemStack;
import dev.Hilligans.Network.Packet.Client.CSendBlockChanges;
import dev.Hilligans.Network.Packet.Client.CUseItem;

import java.nio.DoubleBuffer;

import static org.lwjgl.glfw.GLFW.*;

public class MouseHandler {

    public static MouseHandler instance;

    public long window;

    public boolean mousePressed = false;
    public int button;
    public Client client;

    public  MouseHandler(Client client) {
        this.window = client.window;
        this.client = client;
        instance = this;
    }

    public void invoke(long window, int button, int action, int mods) {
        if(action == GLFW_PRESS) {
            mousePressed = true;
            this.button = button;
            onPress();
        } else if(action == GLFW_RELEASE) {
            mousePressed = false;
        }
    }

    public void onPress() {
        if (client.screen == null) {
            if (button == GLFW_MOUSE_BUTTON_1) {
                BlockPos pos = client.clientWorld.traceBlockToBreak(Camera.pos.x, Camera.pos.y + Camera.playerBoundingBox.eyeHeight, Camera.pos.z, Camera.pitch, Camera.yaw);
                if (pos != null) {
                    if (client.joinServer) {
                        client.sendPacket(new CSendBlockChanges(pos.x, pos.y, pos.z, Blocks.AIR.id));
                    }
                    client.clientWorld.setBlockState(pos, Blocks.AIR.getDefaultState());
                }
            } else if (button == GLFW_MOUSE_BUTTON_2) {
                BlockPos blockPos = client.clientWorld.traceBlockToBreak(Camera.pos.x,Camera.pos.y + Camera.playerBoundingBox.eyeHeight,Camera.pos.z,Camera.pitch,Camera.yaw);
                if(blockPos != null) {
                    BlockState blockState = client.clientWorld.getBlockState(blockPos);
                    if (blockState != null && blockState.getBlock().activateBlock(client.clientWorld, null, blockPos)) {
                        client.sendPacket(new CUseItem((byte) client.playerData.handSlot));
                        return;
                    }
                }
                ItemStack itemStack = client.playerData.inventory.getItem(client.playerData.handSlot);
                if(!itemStack.isEmpty()) {
                    if(itemStack.item.onActivate(client.clientWorld,null)) {
                        client.sendPacket(new CUseItem((byte) client.playerData.handSlot));
                        if(!client.playerData.creative) {
                            itemStack.removeCount(1);
                        }
                    }
                } else {
                    client.sendPacket(new CUseItem((byte) client.playerData.handSlot));
                }
            }
        } else {
            DoubleBuffer doubleBuffer = client.getMousePos();
            client.screen.mouseClick((int) doubleBuffer.get(0), (int) doubleBuffer.get(1), button);
        }
    }
}
