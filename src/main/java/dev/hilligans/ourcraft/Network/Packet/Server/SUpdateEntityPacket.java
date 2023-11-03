package dev.hilligans.ourcraft.Network.Packet.Server;

import dev.hilligans.ourcraft.ClientMain;
import dev.hilligans.ourcraft.Entity.Entity;
import dev.hilligans.ourcraft.Network.IPacketByteArray;
import dev.hilligans.ourcraft.Network.PacketBase;
import dev.hilligans.ourcraft.Network.PacketData;

public class SUpdateEntityPacket extends PacketBase {

    float x;
    float y;
    float z;
    float pitch;
    float yaw;
    int id;

    public SUpdateEntityPacket() {
        super(8);
    }

    public SUpdateEntityPacket(float x, float y, float z, float pitch, float yaw, int id) {
        this();
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = pitch;
        this.yaw = yaw;
        this.id = id;
    }

    public SUpdateEntityPacket(Entity entity) {
        this((float) entity.getX(), (float) entity.getY(), (float) entity.getZ(),entity.pitch,entity.yaw,entity.id);
    }

    @Override
    public void encode(IPacketByteArray packetData) {
        packetData.writeFloat(x);
        packetData.writeFloat(y);
        packetData.writeFloat(z);
        packetData.writeFloat(pitch);
        packetData.writeFloat(yaw);
        packetData.writeInt(id);
    }

    @Override
    public void decode(IPacketByteArray packetData) {
        x = packetData.readFloat();
        y = packetData.readFloat();
        z = packetData.readFloat();
        pitch = packetData.readFloat();
        yaw = packetData.readFloat();
        id = packetData.readInt();
    }

    @Override
    public void handle() {
        if(id != ClientMain.getClient().playerId) {
            Entity entity = ClientMain.getClient().clientWorld.entities.get(id);
            if(entity != null) {
                entity.setPos(x,y,z).setRot(pitch,yaw);
            }
        }
    }
}
