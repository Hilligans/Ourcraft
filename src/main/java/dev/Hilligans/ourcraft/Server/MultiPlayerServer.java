package dev.Hilligans.ourcraft.Server;

import dev.Hilligans.ourcraft.Command.CommandExecutors.ConsoleExecutor;
import dev.Hilligans.ourcraft.Command.Commands;
import dev.Hilligans.ourcraft.Data.Primitives.Tuple;
import dev.Hilligans.ourcraft.Entity.LivingEntities.PlayerEntity;
import dev.Hilligans.ourcraft.GameInstance;
import dev.Hilligans.ourcraft.ModHandler.Events.Server.MultiPlayerServerStartEvent;
import dev.Hilligans.ourcraft.Network.Packet.Client.CHandshakePacket;
import dev.Hilligans.ourcraft.Network.Packet.Server.SDisconnectPacket;
import dev.Hilligans.ourcraft.Network.PacketBase;
import dev.Hilligans.ourcraft.Network.ServerNetwork;
import dev.Hilligans.ourcraft.Network.ServerNetworkHandler;
import dev.Hilligans.ourcraft.Network.ServerNetworkInit;
import dev.Hilligans.ourcraft.Ourcraft;
import dev.Hilligans.ourcraft.World.NewWorldSystem.IServerWorld;
import dev.Hilligans.ourcraft.World.NewWorldSystem.IWorld;
import dev.Hilligans.ourcraft.World.ServerWorld;
import dev.Hilligans.ourcraft.World.World;
import dev.Hilligans.ourcraft.Util.ConsoleReader;
import dev.Hilligans.ourcraft.Util.NamedThreadFactory;
import dev.Hilligans.ourcraft.Util.Settings;
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
    public Int2ObjectOpenHashMap<IWorld> newWorlds = new Int2ObjectOpenHashMap<>();
    public HashMap<ChannelHandlerContext, CHandshakePacket> waitingPlayers = new HashMap<>();
    public HashMap<String, Tuple<ChannelHandlerContext,Long>> playerQueue = new HashMap<>();
    public GameInstance gameInstance = Ourcraft.GAME_INSTANCE;

    public void startServer(String port) {
        gameInstance.EVENT_BUS.postEvent(new MultiPlayerServerStartEvent(this,port));
        Server server = new Server(this);
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1, new NamedThreadFactory("server_tick"));
        executorService.scheduleAtFixedRate(server, 0, 40, TimeUnit.MILLISECONDS);
        ScheduledExecutorService executorService1 = Executors.newScheduledThreadPool(1, new NamedThreadFactory("server_player_handler"));
        executorService1.scheduleAtFixedRate(new PlayerHandler(this), 0, 10, TimeUnit.MILLISECONDS);
        ConsoleReader consoleReader = new ConsoleReader(this::executeCommand);

        ServerNetwork serverNetwork = new ServerNetwork(gameInstance.PROTOCOLS.get("Play"));
        try {
            serverNetwork.startServer(port);
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

    public void addWorld(IServerWorld world) {
        for(int x = -Settings.renderDistance; x < Settings.renderDistance; x++) {
            for(int z = -Settings.renderDistance; z < Settings.renderDistance; z++) {
                world.getChunkNonNull(x * 16L,0,z * 16L);
            }
        }
        newWorlds.put(world.getID(), world);
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
                Tuple<ChannelHandlerContext,Long> player = server.playerQueue.get(key);
                if(player.getTypeB() < time) {
                    server.waitingPlayers.remove(player.typeA);
                    server.playerQueue.remove(key);
                    ServerNetworkHandler.sendPacketClose(new SDisconnectPacket("could not authorize your game"),player.typeA);
                }
            }
        }
    }
}


