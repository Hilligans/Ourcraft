package dev.hilligans.engine;

import dev.hilligans.engine.mod.handler.pipeline.InstanceLoaderPipeline;
import dev.hilligans.engine.mod.handler.pipeline.other.DumpRegistriesPipeline;
import dev.hilligans.engine.mod.handler.pipeline.other.TestPipeline;
import dev.hilligans.engine.mod.handler.pipeline.standard.StandardPipeline;
import dev.hilligans.engine.network.Protocol;
import dev.hilligans.engine.util.Side;
import dev.hilligans.engine.util.argument.Argument;
import dev.hilligans.engine.util.argument.ArgumentContainer;
import dev.hilligans.engine.util.argument.ArgumentSearcher;
import org.lwjgl.system.Configuration;

import java.io.IOException;
import java.util.Arrays;

public class EngineMain {

    public static final long start = System.currentTimeMillis();

    public static final Argument<Boolean> help = Argument.existArg("--help")
            .help("Shows this menu.");
    public static final Argument<Side> startingSide = Argument.sideArg("--side", Side.CLIENT)
            .help("Specifies which side to load.");
    public static final Argument<Boolean> loadImmediate = Argument.existArg("--loadImmediate")
            .help("Always immediately load the GameInstance immediately, otherwise, it's loaded later in the chain before argument parsing. \n" +
                    "This is needed to see any acceptable values from registry arguments");
    public static final Argument<Protocol> defaultProtocol = Argument.registryArg("--protocol", Protocol.class, "ourcraft:Play")
            .help("The network protocol to use");
    public static final Argument<Boolean> runTests = Argument.existArg("--test")
            .help("Loads all contents and performs standard tests");
    public static final Argument<Boolean> debug = Argument.existArg("--debug")
            .help("Turns on engine debuggers and memory trackers.");
    public static final Argument<Boolean> dumpRegistries = Argument.existArg("--dumpRegistries")
            .help("Prints all registry contents after loading");

    public static void main(String[] args) throws IOException {
        ArgumentContainer argumentContainer = new ArgumentContainer(args);
        System.out.println("Starting with arguments: " + Arrays.toString(args));
        System.out.println("Starting client with PID " + ProcessHandle.current().pid());

        GameInstance gameInstance = new GameInstance(argumentContainer);
        gameInstance.side = startingSide.get(gameInstance);

        if(help.get(gameInstance)) {
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
            TestPipeline.run(gameInstance);
        }
        if(dumpRegistries.get(gameInstance)) {
            DumpRegistriesPipeline.run(gameInstance);
        }

        gameInstance.THREAD_PROVIDER.map();
        gameInstance.builtSemaphore.acquireUninterruptibly();

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
