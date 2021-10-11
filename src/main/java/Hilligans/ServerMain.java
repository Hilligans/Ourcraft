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
import io.netty.channel.unix.Buffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL20;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

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
