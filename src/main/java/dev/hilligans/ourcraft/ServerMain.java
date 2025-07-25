package dev.hilligans.ourcraft;

import dev.hilligans.ourcraft.mod.handler.pipeline.standard.StandardPipeline;
import dev.hilligans.ourcraft.server.IServer;
import dev.hilligans.ourcraft.server.MultiPlayerServer;
import dev.hilligans.ourcraft.server.authentication.IAuthenticationScheme;
import dev.hilligans.ourcraft.util.argument.Argument;
import dev.hilligans.ourcraft.util.argument.ArgumentContainer;
import dev.hilligans.ourcraft.util.Profiler;
import dev.hilligans.ourcraft.util.Settings;
import dev.hilligans.ourcraft.util.Side;
import dev.hilligans.ourcraft.util.registry.Registry;
import dev.hilligans.ourcraft.world.newworldsystem.*;
import dev.hilligans.ourcraft.world.gen.IWorldHeightBuilder;
import dev.hilligans.planets.gen.PlanetWorldHeightBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class ServerMain {

    public static Argument<IAuthenticationScheme[]> authenticationSchemes = Argument.arrayRegistryArg("--authenticationSchemes", IAuthenticationScheme.class, "all")
            .help("Specify which authentication schemes to use");


    public static Profiler profiler;
    //public static ServerWorld world;

    public static MultiPlayerServer server;
    public static ArgumentContainer argumentContainer;


    public static void main(String[] args) {
        Ourcraft.argumentContainer = new ArgumentContainer(args);
        Settings.isServer = true;
        GameInstance gameInstance = Ourcraft.GAME_INSTANCE;
        gameInstance.handleArgs(args);
        gameInstance.side = Side.SERVER;
        //gameInstance.loadContent();

        StandardPipeline.get(gameInstance).build();

        server(gameInstance);
    }


    public static IServer server(GameInstance gameInstance) {
        System.out.println("Starting server...");
        gameInstance.builtSemaphore.acquireUninterruptibly();
        gameInstance.builtSemaphore.release();
        System.out.println("Authentication Schemes:" + Arrays.toString(authenticationSchemes.get(gameInstance)));

        gameInstance.THREAD_PROVIDER.map();


        server = new MultiPlayerServer(gameInstance);
        //server.addWorld(0,world);
        //server.addWorld(new SimpleServerWorld(0, "server_world"));
        IServerWorld world1 = new ServerCubicWorld(gameInstance, 0, "planet", 64, new PlanetWorldHeightBuilder(new IWorldHeightBuilder[]{
                new SimpleHeightBuilder(),
                new SimpleHeightBuilder(),
                new SimpleHeightBuilder(),
                new SimpleHeightBuilder(),
                new SimpleHeightBuilder(),
                new SimpleHeightBuilder()
        }, 64).setSeed(1));
        world1.generateWorld();
        System.out.println("Done generating");
        server.addWorld(world1);
        System.out.println("starting server");
        server.startServer("25588");
        gameInstance.THREAD_PROVIDER.unmap();
        return server;
    }

    public static void newServer(GameInstance gameInstance) {

    }

    public static MultiPlayerServer getServer() {
        return server;
    }

    public static void printResults(Process process) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line = "";
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
    }
}
