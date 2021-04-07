package Hilligans.Network.Packet.Client;

import Hilligans.Client.ClientPlayerData;
import Hilligans.ClientMain;
import Hilligans.Data.Other.BlockPos;
import Hilligans.Data.Other.ServerSidedData;
import Hilligans.Entity.Entity;
import Hilligans.Entity.LivingEntities.PlayerEntity;
import Hilligans.Network.Packet.Server.*;
import Hilligans.Data.Other.Server.ServerPlayerData;
import Hilligans.Network.PacketBase;
import Hilligans.Network.PacketData;
import Hilligans.Network.ServerNetworkHandler;
import Hilligans.ServerMain;
import Hilligans.Util.Settings;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelId;


public class CHandshakePacket extends PacketBase {

    public int id;
    public String name;
    public String authToken;
    public long version;

    public CHandshakePacket() {
        super(5);
    }

    @Override
    public void encode(PacketData packetData) {
        packetData.writeInt(Settings.gameVersion);
        packetData.writeString(ClientMain.getClient().playerData.userName);
        packetData.writeLong(ServerSidedData.getInstance().version);
        packetData.writeString(ClientMain.getClient().playerData.authToken);
    }

    @Override
    public void decode(PacketData packetData) {
        id = packetData.readInt();
        name = packetData.readString();
        version = packetData.readLong();
        authToken = packetData.readString();
    }

    @Override
    public void handle() {
        if(id == Settings.gameVersion) {

            int playerId = Entity.getNewId();
            ServerNetworkHandler.sendPacket(new SHandshakePacket(playerId,ServerSidedData.getInstance().version),ctx);
            ServerNetworkHandler.sendPacket(new SChatMessage(name + " has joined the game"));

            ChannelId channelId = ServerNetworkHandler.nameToChannel.get(name);
            BlockPos spawn = ServerMain.getWorld(0).getWorldSpawn(Settings.playerBoundingBox);
            PlayerEntity playerEntity = new PlayerEntity(spawn.x,spawn.y,spawn.z,playerId);
            ServerPlayerData serverPlayerData = new ServerPlayerData(playerEntity);
            for(Entity entity : ServerMain.getWorld(serverPlayerData.getDimension()).entities.values()) {
                ServerNetworkHandler.sendPacket(new SCreateEntityPacket(entity),ctx);
            }
            ServerNetworkHandler.playerData.put(playerId, serverPlayerData);
            ServerNetworkHandler.mappedChannels.put(playerId,ctx.channel().id());
            ServerNetworkHandler.mappedId.put(ctx.channel().id(),playerId);
            ServerNetworkHandler.mappedName.put(ctx.channel().id(),name);
            ServerNetworkHandler.nameToChannel.put(name, ctx.channel().id());
            ServerMain.getWorld(serverPlayerData.getDimension()).addEntity(playerEntity);
            ServerNetworkHandler.sendPacket(new SUpdatePlayer(spawn.x,spawn.y,spawn.z,0,0),ctx);
            serverPlayerData.playerInventory.age++;
            if(version != ServerSidedData.getInstance().version) {
                ServerSidedData.getInstance().sendDataToClient(ctx);
            }

            ServerNetworkHandler.sendPacket(new SUpdateInventory(serverPlayerData.playerInventory),ctx);

        } else {
            try {
                ServerNetworkHandler.sendPacket(new SDisconnectPacket("Game version is incompatible"),ctx).addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture future) throws Exception {
                        if (ctx.channel().isOpen()) {
                            ctx.channel().close().awaitUninterruptibly(100);
                        }
                    }
                });
                //ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
            } catch (Exception ignored) {}
            //ctx.channel().close();
        }
    }
}
