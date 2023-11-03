package dev.hilligans.ourcraft;

import dev.hilligans.ourcraft.Client.Client;
import dev.hilligans.ourcraft.Client.Rendering.Graphics.API.IGraphicsEngine;
import dev.hilligans.ourcraft.Util.ArgumentContainer;
import dev.hilligans.ourcraft.Util.Side;

import java.io.IOException;

public class ClientMain {

    public static Client client;

    public static Client getClient() {
        return client;
    }
    public static GameInstance gameInstance = Ourcraft.GAME_INSTANCE;

    public static ArgumentContainer argumentContainer;

    public static long start = System.currentTimeMillis();
    public static IGraphicsEngine<?,?,?> graphicsEngine = null;

    public static long startTime;
    public static void main(String[] args) throws IOException {
        startTime = System.currentTimeMillis();
        argumentContainer = new ArgumentContainer(args);
        gameInstance.handleArgs(args);
        gameInstance.side = Side.CLIENT;
        gameInstance.loadContent();

        if(argumentContainer.getBoolean("--integratedServer", false)) {
            try {
                Thread thread = new Thread(() -> ServerMain.server(gameInstance, argumentContainer));
                thread.setName("Server-Thread");
                thread.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        client = new Client(gameInstance, argumentContainer);
        String graphicsEngine = argumentContainer.getString("--graphicsEngine", null);
        if(graphicsEngine != null) {
            System.out.println(graphicsEngine);
            client.setGraphicsEngine(gameInstance.GRAPHICS_ENGINES.get(graphicsEngine));
        }
        client.startClient();
    }

    public static int getWindowX() {
        return client == null ? 0 : client.windowX;
    }

    public static int getWindowY() {
        return client == null ? 0 : client.windowY;
    }
}
