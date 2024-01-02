package dev.hilligans.ourcraft.network.packet.client;

import dev.hilligans.ourcraft.data.other.BlockPos;
import dev.hilligans.ourcraft.data.UUID;
import dev.hilligans.ourcraft.entity.Entity;
import dev.hilligans.ourcraft.entity.living.entities.PlayerEntity;
import dev.hilligans.ourcraft.network.*;
import dev.hilligans.ourcraft.data.other.server.ServerPlayerData;
import dev.hilligans.ourcraft.util.Settings;
import dev.hilligans.ourcraft.network.packet.server.SChatMessage;
import dev.hilligans.ourcraft.network.packet.server.SDisconnectPacket;
import dev.hilligans.ourcraft.network.packet.server.SHandshakePacket;
import dev.hilligans.ourcraft.network.packet.server.SSendPlayerList;
import io.netty.channel.ChannelHandlerContext;

import java.security.SecureRandom;
import java.util.Random;


public class CHandshakePacket extends PacketBaseNew<IServerPacketHandler> {

    public int id;
    public String name;
    public String authToken;
    public long version;

    public String username;

    public CHandshakePacket() {
        super(5);
    }

    public CHandshakePacket(String username, String authToken) {
        super(5);
        this.username = username;
        this.authToken = authToken;
    }

    @Override
    public void encode(IPacketByteArray packetData) {
        packetData.writeInt(Settings.gameVersion);
        packetData.writeUTF16(username);
        packetData.writeUTF16(authToken);
    }

    @Override
    public void decode(IPacketByteArray packetData) {
        id = packetData.readInt();
        name = packetData.readUTF16();
        authToken = packetData.readUTF16();
    }

    @Override
    public void handle(IServerPacketHandler serverPacketHandler) {
        if(id == Settings.gameVersion) {
            if(serverPacketHandler.getServerNetworkHandler().nameToPlayerData.get(name) == null || !Settings.forceDifferentName) {
                if (Settings.isOnlineServer) {
                   throw new RuntimeException("reimplement");
                    // String token = getToken(16);
                   // ServerMain.getServer().waitingPlayers.put(ctx, this);
                   // ServerMain.getServer().playerQueue.put(token, new Tuple<>(ctx, System.currentTimeMillis() + 5000));
                   // ClientMain.getClient().authNetwork.sendPacket(new CTokenValid(name, authToken, ((InetSocketAddress) ctx.channel().remoteAddress()).getAddress().getHostAddress(), token));
                } else {
                    handlePlayer(name, version, ctx, name, serverPacketHandler);
                }
            } else {
                ServerNetworkHandler.sendPacketClose(new SDisconnectPacket("An account with your username is already connected to the server"),ctx);
            }
        } else {
            ServerNetworkHandler.sendPacketClose(new SDisconnectPacket("version_incompatible"),ctx);
        }
    }

    public static synchronized void handlePlayer(String name, long version, ChannelHandlerContext ctx, String identifier, IServerPacketHandler serverPacketHandler) {
        int playerId = Entity.getNewId();
       // BlockPos spawn = ServerMain.getWorld(0).getWorldSpawn(Settings.playerBoundingBox);
        BlockPos spawn = new BlockPos(0, 100, 0);
        PlayerEntity playerEntity = new PlayerEntity(spawn.x,spawn.y,spawn.z,playerId);

        ServerPlayerData serverPlayerData = ServerPlayerData.loadOrCreatePlayer(serverPacketHandler.getGameInstance(), playerEntity,identifier);
        playerEntity.setPlayerData(serverPlayerData);
        serverPlayerData.setServer(serverPacketHandler.getServer()).setNetworkHandler(serverPacketHandler.getServerNetworkHandler()).setName(name);
        serverPlayerData.setPlayerID(new UUID(playerId, 0)).setChannelID(ctx.channel().id());

        System.out.println("Handling player");

        serverPacketHandler.getServerNetworkHandler().mappedPlayerData.put(ctx.channel().id(), serverPlayerData);
        //ServerNetworkHandler.mappedName.put(ctx.channel().id(),name);
        serverPacketHandler.getServerNetworkHandler().nameToPlayerData.put(name, serverPlayerData);
        //ServerNetworkHandler.playerData.put(playerId, serverPlayerData);
        serverPlayerData.getWorld().addEntity(playerEntity);
        serverPacketHandler.sendPacket(new SHandshakePacket(playerId, serverPacketHandler.getGameInstance()),ctx);
        serverPlayerData.getWorld().sendChunksToPlayer((int) playerEntity.getX(), (int) playerEntity.getY(), (int) playerEntity.getZ(), serverPlayerData);
        serverPlayerData.getServer().sendPacket(new SChatMessage(name + " has joined the game"));
        //ServerMain.getServer().sendPacket(new SUpdatePlayer(serverPlayerData.));
        serverPacketHandler.getServerNetworkHandler().sendPacketExcept(new SSendPlayerList(name,playerId,true),ctx);

        //ServerMain.getServer().sendPacket(new SSendModContentPacket());

/*
        int size = ServerNetworkHandler.mappedChannels.size();
        String[] players = new String[size];
        int[] ids = new int[size];
        int x = 0;
        for(ChannelId channelId1 : ServerNetworkHandler.mappedChannels.values()) {
            players[x] = ServerNetworkHandler.mappedName.get(channelId1);
            ids[x] = ServerNetworkHandler.mappedId.get(channelId1);
            x++;
        }
        ServerMain.getServer().sendPacket(new SSendPlayerList(players,ids));
        for(Entity entity : ServerMain.getWorld(serverPlayerData.getDimension()).entities.values()) {
            ServerNetworkHandler.sendPacket(new SCreateEntityPacket(entity),ctx);
        }
        ServerNetworkHandler.sendPacket(new SUpdatePlayer(playerEntity.x,playerEntity.y,playerEntity.z,0,0),ctx);
        serverPlayerData.playerInventory.age++;

        if(version != ServerSidedData.getInstance().version) {
            ServerSidedData.getInstance().sendDataToClient(ctx);
        }

 */
    }

    public static final String alphanum = "ABCDEFGHIJKLMNOPQRSTUVQXYZabcdefghijklmnopqrstuvwxyz1234567890`!@#$%^&*()-_=+~[]\\;',./{}|:\"<>?;";
    private static final char[] symbols = alphanum.toCharArray();
    static Random random = new SecureRandom();

    public static String getToken(int length) {
        char[] buf = new char[length];
        int salt = (int) (System.nanoTime() & 31);
        for (int idx = 0; idx < buf.length; ++idx)
            buf[idx] = getChar(Math.abs(Integer.rotateRight(random.nextInt(),salt)));
        return new String(buf);
    }

    public static char getChar(int index) {
        return symbols[index % symbols.length];
    }
}
