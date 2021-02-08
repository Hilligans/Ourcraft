package Hilligans.Network.Packet.Client;

import Hilligans.ClientMain;
import Hilligans.Entity.Entity;
import Hilligans.Entity.LivingEntities.PlayerEntity;
import Hilligans.Network.Packet.Server.SCreateEntityPacket;
import Hilligans.Network.Packet.Server.SHandshakePacket;
import Hilligans.World.Chunk;
import Hilligans.Network.Packet.Server.SChatMessage;
import Hilligans.Network.PacketBase;
import Hilligans.Network.PacketData;
import Hilligans.Network.ServerNetworkHandler;
import Hilligans.ServerMain;
import Hilligans.Util.Settings;


public class CHandshakePacket extends PacketBase {

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
            int containerId = ServerMain.world.getNextContainerID();
            ServerNetworkHandler.sendPacket(new SHandshakePacket(playerId),ctx);
            ServerNetworkHandler.sendPacket(new SChatMessage(name + " has joined the game"));
            for(Entity entity : ServerMain.world.entities.values()) {
                ServerNetworkHandler.sendPacket(new SCreateEntityPacket(entity),ctx);
            }
            PlayerEntity playerEntity = new PlayerEntity(0, Chunk.terrain,0,playerId);
            ServerMain.world.addEntity(playerEntity);
            ServerMain.world.containerInventories.put(containerId,playerEntity.inventory);

            ServerNetworkHandler.mappedChannels.put(playerId,ctx.channel().id());
            ServerNetworkHandler.mappedId.put(ctx.channel().id(),playerId);
            ServerNetworkHandler.mappedName.put(ctx.channel().id(),name);
        } else {
            ctx.channel().close();
        }
    }
}
