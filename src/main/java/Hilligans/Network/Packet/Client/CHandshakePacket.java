package Hilligans.Network.Packet.Client;

import Hilligans.Client.Camera;
import Hilligans.ClientMain;
import Hilligans.Data.Other.BlockPos;
import Hilligans.Entity.Entity;
import Hilligans.Entity.LivingEntities.PlayerEntity;
import Hilligans.Network.Packet.Server.*;
import Hilligans.Server.PlayerData;
import Hilligans.World.Chunk;
import Hilligans.Network.PacketBase;
import Hilligans.Network.PacketData;
import Hilligans.Network.ServerNetworkHandler;
import Hilligans.ServerMain;
import Hilligans.Util.Settings;
import Hilligans.World.ServerWorld;
import io.netty.channel.ChannelId;


public class  CHandshakePacket extends PacketBase {

    public int id;
    public String name;

    public CHandshakePacket() {
        super(5);
    }

    @Override
    public void encode(PacketData packetData) {
        packetData.writeInt(Settings.gameVersion);
        packetData.writeString(ClientMain.name);
      //  packetData.writeString("testabc");
    }

    @Override
    public void decode(PacketData packetData) {
        id = packetData.readInt();
        name = packetData.readString();
      //  test = packetData.readString();
    }

    @Override
    public void handle() {
        if(id == Settings.gameVersion) {
            int playerId = Entity.getNewId();
            ServerNetworkHandler.sendPacket(new SHandshakePacket(playerId),ctx);
            ServerNetworkHandler.sendPacket(new SChatMessage(name + " has joined the game"));
            for(Entity entity : ServerMain.world.entities.values()) {
                ServerNetworkHandler.sendPacket(new SCreateEntityPacket(entity),ctx);
            }
            ChannelId channelId = ServerNetworkHandler.nameToChannel.get(name);
            BlockPos spawn = ServerMain.world.getWorldSpawn(Settings.playerBoundingBox);
            PlayerEntity playerEntity = new PlayerEntity(spawn.x,spawn.y,spawn.z,playerId);
            PlayerData playerData = new PlayerData(playerEntity);
            ServerNetworkHandler.playerData.put(playerId,playerData);
            ServerNetworkHandler.mappedChannels.put(playerId,ctx.channel().id());
            ServerNetworkHandler.mappedId.put(ctx.channel().id(),playerId);
            ServerNetworkHandler.mappedName.put(ctx.channel().id(),name);
            ServerNetworkHandler.nameToChannel.put(name, ctx.channel().id());
            ServerMain.world.addEntity(playerEntity);
            ServerNetworkHandler.sendPacket(new SUpdatePlayer(spawn.x,spawn.y,spawn.z,0,0),ctx);
            playerData.playerInventory.age++;
            ServerNetworkHandler.sendPacket(new SUpdateInventory(playerData.playerInventory),ctx);
        } else {
            ctx.channel().close();
        }
    }
}
