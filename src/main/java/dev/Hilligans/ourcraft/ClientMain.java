package dev.Hilligans.ourcraft;

import dev.Hilligans.ourcraft.Block.Block;
import dev.Hilligans.ourcraft.Block.BlockTypes.ChestBlock;
import dev.Hilligans.ourcraft.Client.Client;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.RenderWindow;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.Vulkan.Boilerplate.Window.VulkanWindow;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.Vulkan.VulkanEngine;
import dev.Hilligans.ourcraft.Command.Commands;
import dev.Hilligans.ourcraft.Resource.DataLoader.ResourceDirectory;
import dev.Hilligans.ourcraft.Resource.DataLoader.ZipResourceDirectory;
import dev.Hilligans.ourcraft.Resource.ResourceLocation;
import dev.Hilligans.ourcraft.Resource.UniversalResourceLoader;
import dev.Hilligans.ourcraft.Tag.CompoundNBTTag;
import dev.Hilligans.ourcraft.Tag.ListNBTTag;
import dev.Hilligans.ourcraft.Tag.NBTTag;
import dev.Hilligans.ourcraft.Util.ArgumentContainer;
import dev.Hilligans.ourcraft.Util.Side;
import dev.Hilligans.ourcraft.WorldSave.WorldLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.zip.ZipFile;

public class ClientMain {

    public static Client client;

    public static Client getClient() {
        return client;
    }
    public static GameInstance gameInstance = Ourcraft.GAME_INSTANCE;

    public static ArgumentContainer argumentContainer;

    public static void main(String[] args) throws IOException {
        argumentContainer = new ArgumentContainer(args);
        gameInstance.handleArgs(args);
        gameInstance.side = Side.CLIENT;
        gameInstance.loadContent();

        client = new Client(gameInstance);

      //  VulkanEngine vulkanEngine = new VulkanEngine(gameInstance);
      //  vulkanEngine.setup();
      //  RenderWindow window = vulkanEngine.createWindow();
       // RenderWindow window1 = vulkanEngine.createWindow();
       // if(window1 instanceof VulkanWindow window2) {
            //window2.selectFamily().graphicsFamily.getQueue(0);
       // }
    //    window.swapBuffers();
        client.startClient();
    }

    public static int getWindowX() {
        return client == null ? 0 : client.windowX;
    }

    public static int getWindowY() {
        return client == null ? 0 : client.windowY;
    }
}
