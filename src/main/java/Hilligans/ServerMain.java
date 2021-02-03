package Hilligans;

import Hilligans.Network.ServerNetworkInit;
import Hilligans.World.ServerWorld;
import Hilligans.World.World;

public class ServerMain {

    public static World world;


    public static void main(String[] args) {
        world = new ServerWorld();
        try {
            ServerNetworkInit.startServer("25586");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
