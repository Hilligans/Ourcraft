package Hilligans.Network.Packet.Server;

import Hilligans.ClientMain;
import Hilligans.Entity.Entity;
import Hilligans.Entity.LivingEntities.PlayerEntity;
import Hilligans.Network.PacketBase;
import Hilligans.Network.PacketData;

public class SCreateEntityPacket extends PacketBase {

    float x;
    float y;
    float z;
    int entityType;
    int id;


    public SCreateEntityPacket() {
        super(9);
    }

    public SCreateEntityPacket(float x, float y, float z, int entityType, int id) {
        this();
        this.x = x;
        this.y = y;
        this.z = z;
        this.entityType = entityType;
        this.id = id;
    }

    public SCreateEntityPacket(Entity entity) {
        this();
        this.x = entity.x;
        this.y = entity.y;
        this.z = entity.z;
        this.entityType = 0;
        this.id = entity.id;
    }

    @Override
    public void encode(PacketData packetData) {
        packetData.writeFloat(x);
        packetData.writeFloat(y);
        packetData.writeFloat(z);
        packetData.writeInt(entityType);
        packetData.writeInt(id);
    }

    @Override
    public void decode(PacketData packetData) {
        x = packetData.readFloat();
        y = packetData.readFloat();
        z = packetData.readFloat();
        entityType = packetData.readInt();
        id = packetData.readInt();
    }

    @Override
    public void handle() {
        ClientMain.clientWorld.entities.put(id,new PlayerEntity(x,y,z,id));
    }
}
