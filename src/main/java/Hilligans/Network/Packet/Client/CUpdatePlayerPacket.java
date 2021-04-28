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

    double x;
    double y;
    double z;
    float pitch;
    float yaw;
    int playerId;

    public CUpdatePlayerPacket() {
        super(7);
    }

    public CUpdatePlayerPacket(double x, double y, double z,float pitch, float yaw, int id) {
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
        packetData.writeDouble(x);
        packetData.writeDouble(y);
        packetData.writeDouble(z);
        packetData.writeFloat(pitch);
        packetData.writeFloat(yaw);
        packetData.writeInt(playerId);
    }

    @Override
    public void decode(PacketData packetData) {
        x = packetData.readDouble();
        y = packetData.readDouble();
        z = packetData.readDouble();
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
                entity.setPos((float)x, (float)y, (float)z).setRot(pitch, yaw);
                ServerNetworkHandler.sendPacket(new SUpdateEntityPacket((float)x, (float)y, (float)z, pitch, yaw, playerId));
            }
        }
    }
}
