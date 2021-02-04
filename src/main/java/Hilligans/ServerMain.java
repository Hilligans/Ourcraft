package Hilligans;

import Hilligans.Client.PlayerMovementThread;
import Hilligans.Network.ServerNetworkInit;
import Hilligans.World.ServerWorld;
import Hilligans.World.World;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ServerMain {

    public static World world;


    public static void main(String[] args) {
        world = new ServerWorld();
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
