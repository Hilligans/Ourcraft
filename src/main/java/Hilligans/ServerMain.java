package Hilligans;

import Hilligans.Block.Blocks;
import Hilligans.Tag.CompoundTag;
import Hilligans.Tag.IntegerTag;
import Hilligans.Tag.Tag;
import Hilligans.Network.ServerNetworkInit;
import Hilligans.Util.Settings;
import Hilligans.World.Builders.OreBuilder;
import Hilligans.World.Chunk;
import Hilligans.World.ServerWorld;
import Hilligans.WorldSave.WorldLoader;
import org.lwjgl.system.CallbackI;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ServerMain {

    public static ServerWorld world;


    public static void main(String[] args) {
        Tag.register();

      //  int val = 3 << 16;
        //System.out.println(val);

        world = new ServerWorld();
        world.worldBuilders.add(new OreBuilder(Blocks.GRASS,Blocks.STONE).setFrequency(20));

        world.generateChunk(0,0);

        for(int x = -Settings.renderDistance; x < Settings.renderDistance; x++) {
            for(int z = -Settings.renderDistance; z < Settings.renderDistance; z++) {
               // System.out.println("GENERATING CHUNK x:" + x + " z:" + z);
                world.generateChunk(x,z);
            }
        }

        //world.setChunk(chunk,0,0);
        //long start = System.currentTimeMillis();
        //WorldLoader.writeChunk(world.getChunk(0,1));
        //System.out.println("Time" + (System.currentTimeMillis() - start));

        Server server = new Server();

        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        executorService.scheduleAtFixedRate(server, 0, 40, TimeUnit.MILLISECONDS);
        try {
            ServerNetworkInit.startServer("25586");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class Server implements Runnable {


        @Override
        public void run() {
           // while(true) {
            //System.out.println("run");
                world.tick();
          //  }
        }
    }
}
