package dev.Hilligans.ourcraft;

import dev.Hilligans.ourcraft.Block.Blocks;
import dev.Hilligans.ourcraft.Server.MultiPlayerServer;
import dev.Hilligans.ourcraft.Util.Profiler;
import dev.Hilligans.ourcraft.Util.Settings;
import dev.Hilligans.ourcraft.World.Builders.OreBuilder;
import dev.Hilligans.ourcraft.World.ServerWorld;
import dev.Hilligans.ourcraft.World.World;

public class ServerMain {


    public static Profiler profiler;
    //public static ServerWorld world;

    public static MultiPlayerServer server;


    public static void main(String[] args) {

        Settings.isServer = true;
        GameInstance gameInstance = Ourcraft.GAME_INSTANCE;

        ServerWorld world = new ServerWorld();
        world.worldBuilders.add(new OreBuilder(Blocks.GRASS,Blocks.STONE).setFrequency(20));

       // Ourcraft.CONTENT_PACK.releaseMod("test_mod");

        server = new MultiPlayerServer();
        server.addWorld(0,world);
        server.startServer("25586");
    }

    public static World getWorld(int id) {
        return server.worlds.get(id);
    }

    public static MultiPlayerServer getServer() {
        return server;
    }

}
