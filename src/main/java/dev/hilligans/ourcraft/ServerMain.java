package dev.hilligans.ourcraft;

import dev.hilligans.ourcraft.Block.Blocks;
import dev.hilligans.ourcraft.Server.MultiPlayerServer;
import dev.hilligans.ourcraft.Util.Profiler;
import dev.hilligans.ourcraft.Util.Settings;
import dev.hilligans.ourcraft.Util.Side;
import dev.hilligans.ourcraft.World.Builders.OreBuilder;
import dev.hilligans.ourcraft.World.NewWorldSystem.*;
import dev.hilligans.ourcraft.World.ServerWorld;
import dev.hilligans.ourcraft.World.World;
import dev.hilligans.ourcraft.World.WorldGen.IWorldHeightBuilder;
import dev.hilligans.planets.gen.PlanetWorldHeightBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ServerMain {


    public static Profiler profiler;
    //public static ServerWorld world;

    public static MultiPlayerServer server;

    public static void main(String[] args) {
        Settings.isServer = true;
        GameInstance gameInstance = Ourcraft.GAME_INSTANCE;
        gameInstance.handleArgs(args);
        gameInstance.side = Side.SERVER;
        gameInstance.loadContent();

        server(gameInstance);
    }

    public static void server(GameInstance gameInstance) {

        ServerWorld world = new ServerWorld(gameInstance);
        world.worldBuilders.add(new OreBuilder("ore", Blocks.GRASS,Blocks.STONE).setFrequency(20));

        server = new MultiPlayerServer();
        server.addWorld(0,world);
        //server.addWorld(new SimpleServerWorld(0, "server_world"));
        IServerWorld world1 = new ServerCubicWorld(0, "planet", 64, new PlanetWorldHeightBuilder(new IWorldHeightBuilder[]{
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
    }

    public static void newServer(GameInstance gameInstance) {

    }

    public static World getWorld(int id) {
        return server.worlds.get(id);
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
