package Hilligans.Server;

import Hilligans.Data.Primitives.DoubleTypeWrapper;
import Hilligans.ModHandler.Events.Server.MultiPlayerServerStartEvent;
import Hilligans.ModHandler.Events.Server.ServerTickEvent;
import Hilligans.Network.Packet.Client.CHandshakePacket;
import Hilligans.Network.Packet.Server.SDisconnectPacket;
import Hilligans.Network.ServerNetworkHandler;
import Hilligans.Network.ServerNetworkInit;
import Hilligans.Ourcraft;
import Hilligans.Util.Settings;
import Hilligans.World.ServerWorld;
import Hilligans.World.World;
import io.netty.channel.ChannelHandlerContext;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MultiPlayerServer {

    public long time = 0;

    public Int2ObjectOpenHashMap<World> worlds = new Int2ObjectOpenHashMap<>();
    public HashMap<ChannelHandlerContext, CHandshakePacket> waitingPlayers = new HashMap<>();
    public HashMap<String,DoubleTypeWrapper<ChannelHandlerContext,Long>> playerQueue = new HashMap<>();

    public void startServer(String port) {
        Ourcraft.EVENT_BUS.postEvent(new MultiPlayerServerStartEvent(this,port));
        Server server = new Server(this);
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        executorService.scheduleAtFixedRate(server, 0, 40, TimeUnit.MILLISECONDS);
        ScheduledExecutorService executorService1 = Executors.newScheduledThreadPool(1);
        executorService1.scheduleAtFixedRate(new PlayerHandler(this), 0, 10, TimeUnit.MILLISECONDS);
        try {
            ServerNetworkInit.startServer(port);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addWorld(int id, World world) {
        ((ServerWorld)world).multiPlayerServer = this;
        world.generateChunk(0,0);
        for(int x = -Settings.renderDistance; x < Settings.renderDistance; x++) {
            for(int z = -Settings.renderDistance; z < Settings.renderDistance; z++) {
                world.generateChunk(x,z);
            }
        }
        worlds.put(id,world);
    }



    static class Server implements Runnable {
        public MultiPlayerServer server;
        public Server(MultiPlayerServer server) {
            this.server = server;
        }
        @Override
        public void run() {
            server.time++;
            Ourcraft.EVENT_BUS.postEvent(new ServerTickEvent(server));
            for(World world : server.worlds.values()) {
                world.tick();
            }
        }
    }

    static class PlayerHandler implements Runnable {

        MultiPlayerServer server;

        public PlayerHandler(MultiPlayerServer multiPlayerServer) {
            this.server = multiPlayerServer;
        }

        @Override
        public void run() {
            long time = System.currentTimeMillis();
            for(String key : server.playerQueue.keySet()) {
                DoubleTypeWrapper<ChannelHandlerContext,Long> player = server.playerQueue.get(key);
                if(player.getTypeB() < time) {
                    server.waitingPlayers.remove(player.typeA);
                    server.playerQueue.remove(key);
                    ServerNetworkHandler.sendPacketClose(new SDisconnectPacket("could not authorize your game"),player.typeA);
                }
            }
        }
    }
}


