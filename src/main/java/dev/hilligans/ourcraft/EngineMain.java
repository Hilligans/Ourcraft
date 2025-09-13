package dev.hilligans.ourcraft;

import dev.hilligans.ourcraft.mod.handler.pipeline.other.TestPipeline;
import dev.hilligans.ourcraft.network.Protocol;
import dev.hilligans.ourcraft.util.argument.Argument;
import dev.hilligans.ourcraft.mod.handler.pipeline.InstanceLoaderPipeline;
import dev.hilligans.ourcraft.mod.handler.pipeline.standard.StandardPipeline;
import dev.hilligans.ourcraft.util.argument.ArgumentContainer;
import dev.hilligans.ourcraft.util.Side;
import dev.hilligans.ourcraft.util.argument.ArgumentSearcher;
import org.lwjgl.system.Configuration;

import static dev.hilligans.ourcraft.Ourcraft.argumentContainer;

import java.io.IOException;
import java.util.Arrays;

public class EngineMain {

    public static final long start = System.currentTimeMillis();

    public static final Argument<Boolean> help = Argument.existArg("--help")
            .help("Shows this menu.");
    public static final Argument<Side> startingSide = Argument.sideArg("--side", Side.CLIENT)
            .help("Specifies which side to load.");
    public static final Argument<Boolean> integratedServer = Argument.existArg("--integratedServer")
            .help("Whether or not to launch an integrated server.");
    public static final Argument<Boolean> loadImmediate = Argument.existArg("--loadImmediate")
            .help("Always immediately load the GameInstance immediately, otherwise, it's loaded later in the chain before argument parsing. \n" +
                    "This is needed to see any acceptable values from registry arguments");
    public static final Argument<Protocol> defaultProtocol = Argument.registryArg("--protocol", Protocol.class, "ourcraft:Play")
            .help("The network protocol to use");
    public static final Argument<Boolean> runTests = Argument.existArg("--test")
            .help("Loads all contents and performs standard tests");
    public static final Argument<Boolean> debug = Argument.existArg("--debug")
            .help("Turns on engine debuggers and memory trackers.");

    public static void main(String[] args) throws IOException {
        Ourcraft.argumentContainer = new ArgumentContainer(args);
        System.out.println("Starting with arguments: " + Arrays.toString(args));
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

        if(debug.get(gameInstance)) {
            Configuration.DEBUG_MEMORY_ALLOCATOR.set(true);
        }

        if(runTests.get(gameInstance)) {
            InstanceLoaderPipeline<?> pipeline = TestPipeline.get(gameInstance);
            pipeline.build();
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

        pipeline.addPostCoreHook(gameInstance1 ->
                gameInstance1.APPLICATIONS.forEach(application ->
                        application.postCoreStartApplication(gameInstance1)));

        pipeline.addPostHook(gameInstance1 ->
                gameInstance1.APPLICATIONS.forEach(application ->
                        application.startApplication(gameInstance1)));

        pipeline.build();
    }
}
