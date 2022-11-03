package dev.Hilligans.ourcraft;

import dev.Hilligans.ourcraft.Client.Client;
import dev.Hilligans.ourcraft.Client.Rendering.Culling.PlaneTestCullingEngine;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.API.IGraphicsEngine;
import dev.Hilligans.ourcraft.Entity.EntityFetcher;
import dev.Hilligans.ourcraft.Util.ArgumentContainer;
import dev.Hilligans.ourcraft.Util.ConsoleReader;
import dev.Hilligans.ourcraft.Util.GameResource.GameResourceTable;
import dev.Hilligans.ourcraft.Util.Logger;
import dev.Hilligans.ourcraft.Util.Side;
import dev.Hilligans.ourcraft.World.Chunk;
import dev.Hilligans.ourcraft.World.ClientWorld;
import it.unimi.dsi.fastutil.longs.Long2BooleanOpenHashMap;
import org.joml.Intersectionf;
import org.joml.Matrix4d;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import java.io.IOException;
import java.util.function.Consumer;

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

        Consumer<EntityFetcher> r = gameInstance::registerEntities;

        new GameResourceTable().createMap(gameInstance);

        if(argumentContainer.getBoolean("--integratedServer", false)) {
            try {
                Thread thread = new Thread(() -> ServerMain.server(gameInstance));
                thread.setName("Server-Thread");
                thread.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        client = new Client(gameInstance);
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
