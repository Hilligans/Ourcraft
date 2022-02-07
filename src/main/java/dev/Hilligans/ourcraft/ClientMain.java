package dev.Hilligans.ourcraft;

import dev.Hilligans.ourcraft.Client.Client;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.IGraphicsEngine;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.Vulkan.Boilerplate.VulkanInstance;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.Vulkan.VulkanEngine;
import dev.Hilligans.ourcraft.Util.ArgumentContainer;
import dev.Hilligans.ourcraft.Util.GameResource.GameResourceTable;
import dev.Hilligans.ourcraft.Util.Side;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jooq.lambda.tuple.Tuple1;
import org.jooq.lambda.tuple.Tuple2;

import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

public class ClientMain {

    public static Client client;

    public static Client getClient() {
        return client;
    }
    public static GameInstance gameInstance = Ourcraft.GAME_INSTANCE;

    public static ArgumentContainer argumentContainer;

    public static long start = System.currentTimeMillis();
    public static IGraphicsEngine<?, ?> graphicsEngine = null;

    public static void main(String[] args) throws IOException {
        argumentContainer = new ArgumentContainer(args);
        gameInstance.handleArgs(args);
        gameInstance.side = Side.CLIENT;
        gameInstance.loadContent();


        new GameResourceTable().createMap(gameInstance);

        client = new Client(gameInstance);
        String graphicsEngine = argumentContainer.getString("--graphicsEngine", null);
        if(graphicsEngine != null) {
            System.out.println(graphicsEngine);
            client.setGraphicsEngine(gameInstance.GRAPHICS_ENGINES.get(graphicsEngine));
        }


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
