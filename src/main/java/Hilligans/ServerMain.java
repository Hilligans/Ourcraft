package Hilligans;

import Hilligans.Blocks.Blocks;
import Hilligans.Client.PlayerMovementThread;
import Hilligans.Network.ServerNetworkInit;
import Hilligans.World.Builders.OreBuilder;
import Hilligans.World.Builders.TreeBuilder;
import Hilligans.World.ServerWorld;
import Hilligans.World.World;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ServerMain {

    public static World world;


    public static void main(String[] args) {
        world = new ServerWorld();
        world.worldBuilders.add(new TreeBuilder().setWorld(world));
        world.worldBuilders.add(new OreBuilder(Blocks.IRON_ORE,Blocks.STONE).setWorld(world).setFrequency(20));
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
