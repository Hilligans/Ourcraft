package dev.Hilligans.Ourcraft.Server;

import dev.Hilligans.Ourcraft.Command.CommandExecutors.ConsoleExecutor;
import dev.Hilligans.Ourcraft.Command.Commands;
import dev.Hilligans.Ourcraft.Data.Primitives.Tuplet;
import dev.Hilligans.Ourcraft.Entity.LivingEntities.PlayerEntity;
import dev.Hilligans.Ourcraft.GameInstance;
import dev.Hilligans.Ourcraft.ModHandler.Events.Server.MultiPlayerServerStartEvent;
import dev.Hilligans.Ourcraft.Network.Packet.Client.CHandshakePacket;
import dev.Hilligans.Ourcraft.Network.Packet.Server.SDisconnectPacket;
import dev.Hilligans.Ourcraft.Network.PacketBase;
import dev.Hilligans.Ourcraft.Network.ServerNetworkHandler;
import dev.Hilligans.Ourcraft.Network.ServerNetworkInit;
import dev.Hilligans.Ourcraft.Ourcraft;
import dev.Hilligans.Ourcraft.World.ServerWorld;
import dev.Hilligans.Ourcraft.World.World;
import dev.Hilligans.Ourcraft.Util.ConsoleReader;
import dev.Hilligans.Ourcraft.Util.NamedThreadFactory;
import dev.Hilligans.Ourcraft.Util.Settings;
import io.netty.channel.ChannelHandlerContext;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MultiPlayerServer implements IServer {

    public long time = 0;

    public Int2ObjectOpenHashMap<World> worlds = new Int2ObjectOpenHashMap<>();
    public HashMap<ChannelHandlerContext, CHandshakePacket> waitingPlayers = new HashMap<>();
    public HashMap<String, Tuplet<ChannelHandlerContext,Long>> playerQueue = new HashMap<>();
    public GameInstance gameInstance = Ourcraft.GAME_INSTANCE;

    public void startServer(String port) {
        gameInstance.EVENT_BUS.postEvent(new MultiPlayerServerStartEvent(this,port));
        Server server = new Server(this);
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1, new NamedThreadFactory("server_tick"));
        executorService.scheduleAtFixedRate(server, 0, 40, TimeUnit.MILLISECONDS);
        ScheduledExecutorService executorService1 = Executors.newScheduledThreadPool(1, new NamedThreadFactory("server_player_handler"));
        executorService1.scheduleAtFixedRate(new PlayerHandler(this), 0, 10, TimeUnit.MILLISECONDS);
        ConsoleReader consoleReader = new ConsoleReader(this::executeCommand);

        try {
            ServerNetworkInit.startServer(port);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addWorld(int id, World world) {
        ((ServerWorld)world).server = this;
        world.generateChunk(0,0);
        for(int x = -Settings.renderDistance; x < Settings.renderDistance; x++) {
            for(int z = -Settings.renderDistance; z < Settings.renderDistance; z++) {
                world.generateChunk( x,z);
            }
        }
        worlds.put(id,world);
    }

    @Override
    public World getWorld(int id) {
        return worlds.get(id);
    }

    @Override
    public Collection<World> getWorlds() {
        return worlds.values();
    }

    @Override
    public long getTime() {
        return time;
    }

    @Override
    public void setTime(long time) {
        this.time = time;
    }

    public Object executeCommand(String command) {
        if(!command.startsWith("/")) {
            command = "/" + command;
        }
        return Commands.executeCommand(command,new ConsoleExecutor(this));
    }

    public World getDefaultWorld() {
        return worlds.values().iterator().next();
    }

    public void sendPacket(PacketBase packetBase) {
        ServerNetworkHandler.sendPacket(packetBase);
    }

    public void sendPacket(PacketBase packetBase, PlayerEntity playerEntity) {
        ServerNetworkHandler.sendPacket(packetBase,playerEntity);
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
                Tuplet<ChannelHandlerContext,Long> player = server.playerQueue.get(key);
                if(player.getTypeB() < time) {
                    server.waitingPlayers.remove(player.typeA);
                    server.playerQueue.remove(key);
                    ServerNetworkHandler.sendPacketClose(new SDisconnectPacket("could not authorize your game"),player.typeA);
                }
            }
        }
    }
}


