package Hilligans;

import Hilligans.Block.Blocks;
import Hilligans.Client.Rendering.Widgets.Widget;
import Hilligans.Item.Items;
import Hilligans.Server.MultiPlayerServer;
import Hilligans.Tag.Tag;
import Hilligans.Util.Profiler;
import Hilligans.Util.Settings;
import Hilligans.World.Builders.OreBuilder;
import Hilligans.World.ServerWorld;
import Hilligans.World.World;

public class ServerMain {


    public static Profiler profiler;
    //public static ServerWorld world;

    public static MultiPlayerServer server;


    public static void main(String[] args) {

        Settings.isServer = true;
        Tag.register();
        Blocks.generateTextures();
        Widget.register();
        Items.register();

        Ourcraft.MOD_LOADER.loadDefaultMods();
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
