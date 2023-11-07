package dev.hilligans.ourcraft.network.packet.client;

import dev.hilligans.ourcraft.data.other.server.ServerPlayerData;
import dev.hilligans.ourcraft.entity.living.entities.PlayerEntity;
import dev.hilligans.ourcraft.network.*;
import dev.hilligans.ourcraft.network.packet.server.SUpdateEntityPacket;
import dev.hilligans.ourcraft.server.IServer;

public class CUpdatePlayerPacket extends PacketBaseNew<IServerPacketHandler> {

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
    public void encode(IPacketByteArray packetData) {
        packetData.writeDouble(x);
        packetData.writeDouble(y);
        packetData.writeDouble(z);
        packetData.writeFloat(pitch);
        packetData.writeFloat(yaw);
        packetData.writeInt(playerId);
    }

    @Override
    public void decode(IPacketByteArray packetData) {
        x = packetData.readDouble();
        y = packetData.readDouble();
        z = packetData.readDouble();
        pitch = packetData.readFloat();
        yaw = packetData.readFloat();
        playerId = packetData.readInt();
    }

    @Override
    public void handle(IServerPacketHandler iServerPacketHandler) {
        ServerPlayerData data = iServerPacketHandler.getServerPlayerData();
        if(data != null) {
            PlayerEntity entity = data.playerEntity;
            if (entity != null) {
                entity.setPos((float)x, (float)y, (float)z).setRot(pitch, yaw);
                IServer server = data.getServer();
                server.sendPacket(new SUpdateEntityPacket((float)x, (float)y, (float)z, pitch, yaw, playerId));
                iServerPacketHandler.getWorld().sendChunksToPlayer((int) x, (int) y, (int) z, data);
            }
        }
    }
}
