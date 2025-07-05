package dev.hilligans.ourcraft;

import dev.hilligans.ourcraft.client.Client;
import dev.hilligans.ourcraft.network.Protocol;
import dev.hilligans.ourcraft.network.engine.INetworkEngine;
import dev.hilligans.ourcraft.util.argument.Argument;
import dev.hilligans.ourcraft.client.rendering.graphics.api.IGraphicsEngine;
import dev.hilligans.ourcraft.mod.handler.pipeline.InstanceLoaderPipeline;
import dev.hilligans.ourcraft.mod.handler.pipeline.standard.StandardPipeline;
import dev.hilligans.ourcraft.util.argument.ArgumentContainer;
import dev.hilligans.ourcraft.util.Side;
import dev.hilligans.ourcraft.util.argument.ArgumentSearcher;
import org.lwjgl.system.Configuration;

import static dev.hilligans.ourcraft.Ourcraft.argumentContainer;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class ClientMain {

    public static final long start = System.currentTimeMillis();

    public static final Argument<Boolean> help = Argument.existArg("--help")
            .help("Shows this menu.");
    public static final Argument<Side> startingSide = Argument.sideArg("--side", Side.CLIENT)
            .help("Specifies which side to load.");
    public static final Argument<Boolean> integratedServer = Argument.existArg("--integrated-server")
            .help("Whether or not to launch an integrated server.");
    public static final Argument<IGraphicsEngine> graphicsEngine = Argument.registryArg("--graphicsEngine", IGraphicsEngine.class, "ourcraft:openglEngine")
            .help("The default graphics engine to use, still need to lookup acceptable values based on registry.");
    public static final Argument<Boolean> loadImmediate = Argument.existArg("--loadImmediate")
            .help("Always immediately load the GameInstance immediately, otherwise, it's loaded later in the chain before argument parsing. \n" +
                    "This is needed to see any acceptable values from registry arguments");
    public static final Argument<Protocol> defaultProtocol = Argument.registryArg("--protocol", Protocol.class, "ourcraft:Play")
            .help("The network protocol to use");



    public static void main(String[] args) throws IOException {
        //Configuration.DEBUG_MEMORY_ALLOCATOR.set(true);
        Ourcraft.argumentContainer = new ArgumentContainer(args);
        System.out.println("Starting with arguments: " + Arrays.toString(args));
        //System.out.println(STR."Starting client with PID \{ProcessHandle.current().pid()}");
        System.out.println("Starting client with PID " + ProcessHandle.current().pid());

        GameInstance gameInstance = Ourcraft.GAME_INSTANCE;
        gameInstance.handleArgs(args);
        gameInstance.side = startingSide.get(argumentContainer);

        if(help.get(argumentContainer)) {
            if(loadImmediate.get(gameInstance)) {
                StandardPipeline.get(gameInstance).build();
            }
            System.out.println(ArgumentSearcher.findAllArguments(gameInstance));
            System.exit(0);
        }


        gameInstance.THREAD_PROVIDER.map();
        gameInstance.builtSemaphore.acquireUninterruptibly();

        if(integratedServer.get(argumentContainer)) {
            try {
                Thread serverThread = new Thread(() -> ServerMain.server(gameInstance));
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
                client.get().setGraphicsEngine(graphicsEngine.get(gameInstance));

                client.get().startClient();
                waiting.release();

                client.get().loop();

                gameInstance1.THREAD_PROVIDER.EXECUTOR.shutdownNow();
                if(integratedServer.get(gameInstance)) {
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

        pipeline.addPostHook(gameInstance13 -> {
            Thread thread = new Thread() {
                @Override
                public void run() {
                    INetworkEngine<?, ?> engine = gameInstance13.getExcept("ourcraft:nettyEngine", INetworkEngine.class);
                    engine.openServer(defaultProtocol.get(gameInstance13), "10000");
                }
            };
            thread.setDaemon(true);
            thread.start();
        });

        pipeline.addPostHook(gameInstance1 -> {
            INetworkEngine<?, ?> engine = gameInstance1.getExcept("ourcraft:nettyEngine", INetworkEngine.class);
            engine.openClient(defaultProtocol.get(gameInstance1), "localhost", "10000");
        });

        pipeline.build();
    }
}
