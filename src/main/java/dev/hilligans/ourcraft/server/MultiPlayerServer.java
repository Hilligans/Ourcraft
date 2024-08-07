package dev.hilligans.ourcraft.server;

import dev.hilligans.ourcraft.command.executors.ConsoleExecutor;
import dev.hilligans.ourcraft.command.Commands;
import dev.hilligans.ourcraft.data.other.server.ServerPlayerData;
import dev.hilligans.ourcraft.data.primitives.Tuple;
import dev.hilligans.ourcraft.entity.IPlayerEntity;
import dev.hilligans.ourcraft.entity.living.entities.PlayerEntity;
import dev.hilligans.ourcraft.GameInstance;
import dev.hilligans.ourcraft.mod.handler.events.server.MultiPlayerServerStartEvent;
import dev.hilligans.ourcraft.network.PacketBase;
import dev.hilligans.ourcraft.network.packet.client.CHandshakePacket;
import dev.hilligans.ourcraft.network.packet.server.SDisconnectPacket;
import dev.hilligans.ourcraft.network.ServerNetwork;
import dev.hilligans.ourcraft.network.ServerNetworkHandler;
import dev.hilligans.ourcraft.Ourcraft;
import dev.hilligans.ourcraft.world.newworldsystem.IServerWorld;
import dev.hilligans.ourcraft.util.NamedThreadFactory;
import dev.hilligans.ourcraft.util.Settings;
import dev.hilligans.ourcraft.world.newworldsystem.IWorld;
import io.netty.channel.ChannelHandlerContext;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class MultiPlayerServer implements IServer {

    public long time = 0;

    public Int2ObjectOpenHashMap<IServerWorld> newWorlds = new Int2ObjectOpenHashMap<>();
    public HashMap<ChannelHandlerContext, CHandshakePacket> waitingPlayers = new HashMap<>();
    public HashMap<String, Tuple<ChannelHandlerContext,Long>> playerQueue = new HashMap<>();
    public GameInstance gameInstance;
    public ServerNetwork serverNetwork;
    public boolean running = true;
    public ScheduledExecutorService tick;
    public ScheduledExecutorService playerHandler;

    public int renderDistance = 32 * 4;

    public MultiPlayerServer(GameInstance gameInstance) {
        this.gameInstance = gameInstance;
    }

    public void startServer(String port) {
        gameInstance.EVENT_BUS.postEvent(new MultiPlayerServerStartEvent(this,port));
        Server server = new Server(this);
        tick = Executors.newScheduledThreadPool(1, new NamedThreadFactory("server_tick", gameInstance));
        tick.scheduleAtFixedRate(server, 0, 40, TimeUnit.MILLISECONDS);
        playerHandler = Executors.newScheduledThreadPool(1, new NamedThreadFactory("server_player_handler", gameInstance));
        playerHandler.scheduleAtFixedRate(new PlayerHandler(this), 0, 10, TimeUnit.MILLISECONDS);
       // ConsoleReader consoleReader = new ConsoleReader(this::executeCommand);

        serverNetwork = new ServerNetwork(gameInstance, gameInstance.PROTOCOLS.get("ourcraft:Play"), this).debug(Ourcraft.getArgumentContainer().getBoolean("--packetTrace", false));
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

    @Override
    public void tick() {

    }

    public Object executeCommand(String command) {
        if(!command.startsWith("/")) {
            command = "/" + command;
        }
        return Commands.executeCommand(command,new ConsoleExecutor(this));
    }

    @Override
    public ServerNetworkHandler getServerNetworkHandler() {
        return (ServerNetworkHandler) serverNetwork.networkHandler;
    }

    @Override
    public void sendPacketToAllVisible(PacketBase<?> packet, long x, long y, long z, IWorld serverWorld) {
        serverWorld.forEachPlayerInRange(x, y, z, renderDistance, iPlayerEntity -> sendPacket(packet, iPlayerEntity.getPlayerData()));
    }

    public void sendPacket(PacketBase<?> packetBase) {
        getServerNetworkHandler().sendPacketInternal(packetBase);
    }

    public void sendPacket(PacketBase<?> packetBase, PlayerEntity playerEntity) {
        getServerNetworkHandler().sendPacket(packetBase,playerEntity);
    }

    @Override
    public void sendPacket(PacketBase<?> packetBase, ServerPlayerData playerData) {
        getServerNetworkHandler().sendPacket(packetBase, playerData.getChannelId());
    }

    @Override
    public GameInstance getGameInstance() {
        return gameInstance;
    }

    @Override
    public void stop() {
        running = false;
        tick.shutdownNow();
        playerHandler.shutdownNow();
        if(serverNetwork != null) {
            serverNetwork.close();
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


