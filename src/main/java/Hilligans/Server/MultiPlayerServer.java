package Hilligans.Server;

import Hilligans.Network.ServerNetworkInit;
import Hilligans.ServerMain;
import Hilligans.Util.Settings;
import Hilligans.World.World;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MultiPlayerServer {

    public Int2ObjectOpenHashMap<World> worlds = new Int2ObjectOpenHashMap<>();

    public void startServer(String port) {
        Server server = new Server(worlds);
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        executorService.scheduleAtFixedRate(server, 0, 40, TimeUnit.MILLISECONDS);
        try {
            ServerNetworkInit.startServer(port);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addWorld(int id, World world) {
        world.generateChunk(0,0);
        for(int x = -Settings.renderDistance; x < Settings.renderDistance; x++) {
            for(int z = -Settings.renderDistance; z < Settings.renderDistance; z++) {
                world.generateChunk(x,z);
            }
        }
        worlds.put(id,world);
    }



    static class Server implements Runnable {
        public Int2ObjectOpenHashMap<World> worlds;
        public Server(Int2ObjectOpenHashMap<World> worlds) {
            this.worlds = worlds;
        }
        @Override
        public void run() {
            for(World world : worlds.values()) {
                world.tick();
            }
        }
    }
}


