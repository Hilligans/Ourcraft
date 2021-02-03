package Hilligans;

import Hilligans.Network.ServerNetworkInit;
import Hilligans.World.ServerWorld;
import Hilligans.World.World;

public class ServerMain {

    public static World world;


    public static void main(String[] args) {

        //System.out.println(("" + "a").length());

        world = new ServerWorld();
        try {
            ServerNetworkInit.startServer("25586");
        } catch (Exception e) {
            e.printStackTrace();
        }


        //ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
       // executorService.scheduleAtFixedRate(playerMovementThread, 0, 5, TimeUnit.MILLISECONDS);


    }
}
