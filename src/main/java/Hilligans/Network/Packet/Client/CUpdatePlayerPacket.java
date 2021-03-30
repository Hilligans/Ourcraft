package Hilligans.Network.Packet.Client;

import Hilligans.Data.Other.Server.ServerPlayerData;
import Hilligans.Data.Other.ServerSidedData;
import Hilligans.Entity.Entity;
import Hilligans.Network.Packet.Server.SUpdateEntityPacket;
import Hilligans.Network.PacketBase;
import Hilligans.Network.PacketData;
import Hilligans.Network.ServerNetworkHandler;
import Hilligans.ServerMain;

public class CUpdatePlayerPacket extends PacketBase {

    float x;
    float y;
    float z;
    float pitch;
    float yaw;
    int playerId;

    public CUpdatePlayerPacket() {
        super(7);
    }

    public CUpdatePlayerPacket(float x, float y, float z,float pitch, float yaw, int id) {
        this();
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = pitch;
        this.yaw = yaw;
        this.playerId = id;
    }

    @Override
    public void encode(PacketData packetData) {
        packetData.writeFloat(x);
        packetData.writeFloat(y);
        packetData.writeFloat(z);
        packetData.writeFloat(pitch);
        packetData.writeFloat(yaw);
        packetData.writeInt(playerId);
    }

    @Override
    public void decode(PacketData packetData) {
        x = packetData.readFloat();
        y = packetData.readFloat();
        z = packetData.readFloat();
        pitch = packetData.readFloat();
        yaw = packetData.readFloat();
        playerId = packetData.readInt();
    }

    @Override
    public void handle() {
        ServerPlayerData data = ServerNetworkHandler.getPlayerData(ctx) ;
        if(data != null) {
            int dim = data.getDimension();
            Entity entity = ServerMain.getWorld(dim).entities.get(playerId);
            if (entity != null) {
                entity.setPos(x, y, z).setRot(pitch, yaw);
                ServerNetworkHandler.sendPacket(new SUpdateEntityPacket(x, y, z, pitch, yaw, playerId));
            }
        }
    }
}
