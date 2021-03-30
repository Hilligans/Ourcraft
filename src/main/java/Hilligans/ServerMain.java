package Hilligans;

import Hilligans.Block.Blocks;
import Hilligans.Client.Camera;
import Hilligans.Client.Rendering.Widgets.Widget;
import Hilligans.Data.Other.BlockPos;
import Hilligans.Data.Other.BoundingBox;
import Hilligans.Data.Other.ServerSidedData;
import Hilligans.Server.MultiPlayerServer;
import Hilligans.Tag.CompoundTag;
import Hilligans.Tag.IntegerTag;
import Hilligans.Tag.Tag;
import Hilligans.Network.ServerNetworkInit;
import Hilligans.Util.Settings;
import Hilligans.World.Builders.OreBuilder;
import Hilligans.World.Chunk;
import Hilligans.World.DataProvider;
import Hilligans.World.ServerWorld;
import Hilligans.World.World;
import Hilligans.WorldSave.WorldLoader;
import it.unimi.dsi.fastutil.shorts.Short2ObjectOpenHashMap;
import org.lwjgl.system.CallbackI;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ServerMain {

    //public static ServerWorld world;

    public static MultiPlayerServer server;


    public static void main(String[] args) {
        Settings.isServer = true;
        Tag.register();
        Blocks.generateTextures();
        ServerSidedData.getInstance().register();
        Widget.register();

        ServerWorld world = new ServerWorld();
        world.worldBuilders.add(new OreBuilder(Blocks.GRASS,Blocks.STONE).setFrequency(20));

        server = new MultiPlayerServer();
        server.addWorld(0,world);
        server.startServer("25586");
    }

    public static World getWorld(int id) {
        return server.worlds.get(id);
    }

}
