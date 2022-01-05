package dev.Hilligans.ourcraft;

import dev.Hilligans.ourcraft.Block.Block;
import dev.Hilligans.ourcraft.Block.BlockTypes.ChestBlock;
import dev.Hilligans.ourcraft.Client.Client;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.RenderWindow;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.Vulkan.VulkanEngine;
import dev.Hilligans.ourcraft.Command.Commands;
import dev.Hilligans.ourcraft.Tag.CompoundNBTTag;
import dev.Hilligans.ourcraft.Tag.ListNBTTag;
import dev.Hilligans.ourcraft.Tag.NBTTag;
import dev.Hilligans.ourcraft.WorldSave.WorldLoader;

import java.util.Arrays;

public class ClientMain {

    public static Client client;

    public static Client getClient() {
        return client;
    }
    public static GameInstance gameInstance = Ourcraft.GAME_INSTANCE;


    public static void main(String[] args) {
        gameInstance.handleArgs(args);
        client = new Client(gameInstance);
        client.startClient();
    }

    public static void handleArgs(String[] args) {
        for(String string : args) {
            if(string.length() >= 5 && string.startsWith("--path")) {
                Ourcraft.path = string.substring(5);
            }
        }
    }

    public static int getWindowX() {
        return client == null ? 0 : client.windowX;
    }

    public static int getWindowY() {
        return client == null ? 0 : client.windowY;
    }


}
