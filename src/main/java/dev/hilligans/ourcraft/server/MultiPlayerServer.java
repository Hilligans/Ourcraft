package dev.hilligans.ourcraft.server;

import dev.hilligans.engine.GameInstance;
import dev.hilligans.ourcraft.data.other.BlockPos;
import dev.hilligans.ourcraft.data.other.server.ServerPlayerData;
import dev.hilligans.engine.data.Tuple;
import dev.hilligans.ourcraft.entity.Entity;
import dev.hilligans.ourcraft.entity.living.entities.PlayerEntity;
import dev.hilligans.engine.mod.handler.events.server.MultiPlayerServerStartEvent;
import dev.hilligans.engine.network.AuthenticationException;
import dev.hilligans.engine.network.Protocol;
import dev.hilligans.engine.network.engine.INetworkEngine;
import dev.hilligans.engine.network.engine.NetworkEntity;
import dev.hilligans.engine.network.engine.NetworkSocket;
import dev.hilligans.engine.authentication.IAccount;
import dev.hilligans.engine.authentication.IAuthenticationScheme;
import dev.hilligans.engine.util.IByteArray;
import dev.hilligans.engine.util.NamedThreadFactory;
import dev.hilligans.ourcraft.util.Settings;
import dev.hilligans.ourcraft.world.newworldsystem.IServerWorld;
import io.netty.channel.ChannelHandlerContext;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MultiPlayerServer implements IServer {

    public long time = 0;

    public Int2ObjectOpenHashMap<IServerWorld> newWorlds = new Int2ObjectOpenHashMap<>();
    public HashMap<String, Tuple<ChannelHandlerContext,Long>> playerQueue = new HashMap<>();
    public GameInstance gameInstance;
    public boolean running = true;
    public ScheduledExecutorService tick;
    public ScheduledExecutorService playerHandler;
    public ArrayList<NetworkSocket<?>> networkSockets = new ArrayList<>();
    public ConcurrentHashMap<ServerPlayerData, Boolean> playerMap = new ConcurrentHashMap<>();

    public int renderDistance = 32 * 4;

    public HashMap<String, IAuthenticationScheme<?>> authenticationSchemes = new HashMap<>();

    public MultiPlayerServer(GameInstance gameInstance, IAuthenticationScheme<?>[] authenticationSchemes) {
        this.gameInstance = gameInstance;
        for(IAuthenticationScheme<?> authenticationScheme : authenticationSchemes) {
            this.authenticationSchemes.put(authenticationScheme.getIdentifierName(), authenticationScheme);
        }
    }

    public void startServer(String port) {
        gameInstance.EVENT_BUS.postEvent(new MultiPlayerServerStartEvent(this,port));
        Server server = new Server(this);
        tick = Executors.newScheduledThreadPool(1, new NamedThreadFactory("server_tick", gameInstance));
        tick.scheduleAtFixedRate(server, 0, 40, TimeUnit.MILLISECONDS);
        playerHandler = Executors.newScheduledThreadPool(1, new NamedThreadFactory("server_player_handler", gameInstance));
        playerHandler.scheduleAtFixedRate(new PlayerHandler(this), 0, 10, TimeUnit.MILLISECONDS);
       // ConsoleReader consoleReader = new ConsoleReader(this::executeCommand);

        INetworkEngine<?, ?> engine = gameInstance.getExcept("ourcraft:nettyEngine", INetworkEngine.class);
        NetworkSocket<?> socket = engine.openServer(gameInstance.PROTOCOLS.getExcept("ourcraft:login"), this, "25588");
        networkSockets.add(socket);

        //serverNetwork = new ServerNetwork(gameInstance, gameInstance.PROTOCOLS.getExcept("ourcraft:Play"), this).debug(Ourcraft.getArgumentContainer().getBoolean("--packetTrace", false));
        try {
            socket.connectSocket();
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
    public Iterable<IServerWorld> getWorlds() {
        return newWorlds.values();
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
        return null;
        //return Commands.executeCommand(command,new ConsoleExecutor(this));
    }

    @Override
    public void sendPacket(Protocol matchingProtocol, IByteArray array) {
        playerMap.forEachKey(Integer.MAX_VALUE, (key) -> {if(key.networkEntity.getSendProtocol().equals(matchingProtocol)) { key.sendPacket(array);}});
    }

    @Override
    public ServerPlayerData loadPlayer(String player, NetworkEntity entity) {
        int playerId = Entity.getNewId();
        // BlockPos spawn = ServerMain.getWorld(0).getWorldSpawn(Settings.playerBoundingBox);
        BlockPos spawn = new BlockPos(0, 100, 0);
        PlayerEntity playerEntity = new PlayerEntity(spawn.x,spawn.y,spawn.z,playerId);

        ServerPlayerData serverPlayerData = ServerPlayerData.loadOrCreatePlayer(getGameInstance(), playerEntity, player);
        serverPlayerData.networkEntity = entity;
        playerEntity.setPlayerData(serverPlayerData);
        serverPlayerData.setServer(this);
        playerMap.put(serverPlayerData, true);

        return serverPlayerData;
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
        networkSockets.forEach(NetworkSocket::closeSocket);
    }

    @Override
    public IAccount<?> authenticate(String scheme, String username, IByteArray data) {
        IAuthenticationScheme<?> authenticationScheme = authenticationSchemes.get(scheme);
        if(authenticationScheme == null) {
            throw new AuthenticationException("Unknown authentication scheme: " + scheme);
        }

        return authenticationScheme.authenticate(username, data);
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
                    //server.waitingPlayers.remove(player.typeA);
                    //server.playerQueue.remove(key);
                    //ServerNetworkHandler.sendPacketClose(new SDisconnectPacket("could not authorize your game"),player.typeA);
                }
            }
        }
    }
}


