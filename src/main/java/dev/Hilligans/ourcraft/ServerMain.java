package dev.Hilligans.ourcraft;

import dev.Hilligans.ourcraft.Block.Blocks;
import dev.Hilligans.ourcraft.Server.MultiPlayerServer;
import dev.Hilligans.ourcraft.Util.Profiler;
import dev.Hilligans.ourcraft.Util.Settings;
import dev.Hilligans.ourcraft.Util.Side;
import dev.Hilligans.ourcraft.World.Builders.OreBuilder;
import dev.Hilligans.ourcraft.World.Chunk;
import dev.Hilligans.ourcraft.World.ServerWorld;
import dev.Hilligans.ourcraft.World.World;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.atomic.AtomicInteger;

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
        System.out.println("starting server");
        server.startServer("25588");
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
