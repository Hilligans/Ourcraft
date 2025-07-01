package dev.hilligans.ourcraft;

import dev.hilligans.ourcraft.client.Client;
import dev.hilligans.ourcraft.client.rendering.graphics.api.IGraphicsEngine;
import dev.hilligans.ourcraft.mod.handler.pipeline.InstanceLoaderPipeline;
import dev.hilligans.ourcraft.mod.handler.pipeline.standard.StandardPipeline;
import dev.hilligans.ourcraft.util.ArgumentContainer;
import dev.hilligans.ourcraft.util.Side;

import static dev.hilligans.ourcraft.Ourcraft.argumentContainer;

import java.io.IOException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicReference;

public class ClientMain {

    public static long start = System.currentTimeMillis();
    public static IGraphicsEngine<?,?,?> graphicsEngine = null;

    public static long startTime;
    public static void main(String[] args) throws IOException {
        Ourcraft.argumentContainer = new ArgumentContainer(args);

        //System.out.println(STR."Starting client with PID \{ProcessHandle.current().pid()}");
        System.out.println("Starting client with PID " + ProcessHandle.current().pid());

        GameInstance gameInstance = Ourcraft.GAME_INSTANCE;
        gameInstance.handleArgs(args);
        gameInstance.side = Side.CLIENT;
        gameInstance.THREAD_PROVIDER.map();

        gameInstance.builtSemaphore.acquireUninterruptibly();

        Thread serverThread = null;
        if(argumentContainer.getBoolean("--integratedServer", false)) {
            try {
                serverThread = new Thread(() -> ServerMain.server(gameInstance));
                serverThread.setName("Server-Thread");
                serverThread.setDaemon(true);
                serverThread.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        InstanceLoaderPipeline<?> pipeline = StandardPipeline.get(gameInstance);

        AtomicReference<Client> client = new AtomicReference<>();

        pipeline.addPostCoreHook(gameInstance1 -> {
            Semaphore waiting = new Semaphore(1);
            try {
                waiting.acquire();
            } catch (Exception ignored) {}
            new Thread(() -> {
                gameInstance1.THREAD_PROVIDER.map();
                client.set(new Client(gameInstance1, argumentContainer));

                String graphicsEngine = argumentContainer.getString("--graphicsEngine", null);
                if(graphicsEngine != null) {
                    client.get().setGraphicsEngine(gameInstance1.GRAPHICS_ENGINES.get(graphicsEngine));
                }
                client.get().startClient();
                waiting.release();

                client.get().loop();

                gameInstance1.THREAD_PROVIDER.EXECUTOR.shutdownNow();
                if(argumentContainer.getBoolean("--integratedServer", false)) {
                    ServerMain.getServer().stop();
                }
                System.exit(0);

            }).start();

            try {
                waiting.acquire();
                waiting.release();
            } catch (Exception ignored) {}
        });

        pipeline.addPostHook(gameInstance12 -> {
            while(client.get() == null) {}
            client.get().transition = true;
        });

        pipeline.build();

    }
}
