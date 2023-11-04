package dev.hilligans.ourcraft.Server;

import dev.hilligans.ourcraft.Command.CommandExecutors.ConsoleExecutor;
import dev.hilligans.ourcraft.Command.Commands;
import dev.hilligans.ourcraft.Data.Other.Server.ServerPlayerData;
import dev.hilligans.ourcraft.Data.Primitives.Tuple;
import dev.hilligans.ourcraft.Entity.LivingEntities.PlayerEntity;
import dev.hilligans.ourcraft.GameInstance;
import dev.hilligans.ourcraft.ModHandler.Events.Server.MultiPlayerServerStartEvent;
import dev.hilligans.ourcraft.Network.Packet.Client.CHandshakePacket;
import dev.hilligans.ourcraft.Network.Packet.Server.SDisconnectPacket;
import dev.hilligans.ourcraft.Network.PacketBase;
import dev.hilligans.ourcraft.Network.ServerNetwork;
import dev.hilligans.ourcraft.Network.ServerNetworkHandler;
import dev.hilligans.ourcraft.Ourcraft;
import dev.hilligans.ourcraft.ServerMain;
import dev.hilligans.ourcraft.World.NewWorldSystem.IServerWorld;
import dev.hilligans.ourcraft.World.NewWorldSystem.IWorld;
import dev.hilligans.ourcraft.World.ServerWorld;
import dev.hilligans.ourcraft.World.World;
import dev.hilligans.ourcraft.Util.ConsoleReader;
import dev.hilligans.ourcraft.Util.NamedThreadFactory;
import dev.hilligans.ourcraft.Util.Settings;
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
    public Int2ObjectOpenHashMap<IServerWorld> newWorlds = new Int2ObjectOpenHashMap<>();
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

        ServerNetwork serverNetwork = new ServerNetwork(gameInstance.PROTOCOLS.get("Play")).debug(ServerMain.argumentContainer.getBoolean("--packetTrace", false));
        try {
            serverNetwork.startServer(port);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addWorld(IServerWorld world) {
        world.setServer(this);
        for(int x = -Settings.renderDistance; x < Settings.renderDistance; x++) {
            for(int z = -Settings.renderDistance; z < Settings.renderDistance; z++) {
                world.getChunkNonNull(x * 16L,0,z * 16L);
            }
        }
        newWorlds.put(world.getID(), world);
    }

    @Override
    public IServerWorld getWorld(ServerPlayerData serverPlayerData) {
        return newWorlds.get(serverPlayerData.getDimension());
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


