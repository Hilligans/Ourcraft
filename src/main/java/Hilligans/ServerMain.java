package Hilligans;

import Hilligans.Block.Blocks;
import Hilligans.Client.Camera;
import Hilligans.Client.Rendering.Widgets.Widget;
import Hilligans.Data.Other.BlockPos;
import Hilligans.Data.Other.BoundingBox;
import Hilligans.Data.Other.ServerSidedData;
import Hilligans.Network.ClientAuthNetworkHandler;
import Hilligans.Network.ClientNetworkHandler;
import Hilligans.Network.ClientNetworkInit;
import Hilligans.Network.Packet.AuthServerPackets.CTokenValid;
import Hilligans.Server.MultiPlayerServer;
import Hilligans.Tag.CompoundTag;
import Hilligans.Tag.IntegerTag;
import Hilligans.Tag.Tag;
import Hilligans.Network.ServerNetworkInit;
import Hilligans.Util.ConsoleReader;
import Hilligans.Util.Profiler;
import Hilligans.Util.Settings;
import Hilligans.World.Builders.OreBuilder;
import Hilligans.World.Chunk;
import Hilligans.World.DataProvider;
import Hilligans.World.ServerWorld;
import Hilligans.World.World;
import Hilligans.WorldSave.WorldLoader;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import it.unimi.dsi.fastutil.shorts.Short2ObjectOpenHashMap;
import org.lwjgl.system.CallbackI;

import javax.imageio.ImageIO;
import java.io.File;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ServerMain {


    public static Profiler profiler;
    //public static ServerWorld world;

    public static MultiPlayerServer server;


    public static void main(String[] args) {

        Settings.isServer = true;
        Tag.register();
        Blocks.generateTextures();
        ServerSidedData.getInstance().register();
        Widget.register();

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
