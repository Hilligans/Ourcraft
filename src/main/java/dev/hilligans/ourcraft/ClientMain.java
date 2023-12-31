package dev.hilligans.ourcraft;

import dev.hilligans.ourcraft.client.Client;
import dev.hilligans.ourcraft.client.rendering.graphics.api.IGraphicsEngine;
import dev.hilligans.ourcraft.client.rendering.newrenderer.TextureAtlas;
import dev.hilligans.ourcraft.data.other.BoundingBox;
import dev.hilligans.ourcraft.util.ArgumentContainer;
import dev.hilligans.ourcraft.util.Side;
import org.joml.Intersectionf;
import org.joml.Math;
import org.joml.Vector2f;

import java.io.IOException;

public class ClientMain {

    //public static GameInstance gameInstance = Ourcraft.GAME_INSTANCE;

    public static ArgumentContainer argumentContainer;

    public static long start = System.currentTimeMillis();
    public static IGraphicsEngine<?,?,?> graphicsEngine = null;

    public static long startTime;
    public static void main(String[] args) throws IOException {
        startTime = System.currentTimeMillis();
        argumentContainer = new ArgumentContainer(args);

        System.out.println(STR."Starting client with PID \{ProcessHandle.current().pid()}");

        GameInstance gameInstance = Ourcraft.GAME_INSTANCE;
        gameInstance.handleArgs(args);
        gameInstance.side = Side.CLIENT;
        gameInstance.loadContent();

        Thread serverThread = null;
        if(argumentContainer.getBoolean("--integratedServer", false)) {
            try {
                serverThread = new Thread(() -> ServerMain.server(gameInstance, argumentContainer));
                serverThread.setName("Server-Thread");
                serverThread.setDaemon(true);
                serverThread.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Client client = new Client(gameInstance, argumentContainer);
       // Client client1 = new Client(gameInstance, argumentContainer);
        String graphicsEngine = argumentContainer.getString("--graphicsEngine", null);
        if(graphicsEngine != null) {
            System.out.println(graphicsEngine);
            client.setGraphicsEngine(gameInstance.GRAPHICS_ENGINES.get(graphicsEngine));
          //  client1.setGraphicsEngine(gameInstance.GRAPHICS_ENGINES.get(graphicsEngine));
        }
        //client1.setupClient();
        client.startClient();
        Ourcraft.EXECUTOR.shutdownNow();
        TextureAtlas.EXECUTOR.shutdownNow();
        if(argumentContainer.getBoolean("--integratedServer", false)) {
            ServerMain.getServer().stop();
        }
        System.exit(0);
    }
}
