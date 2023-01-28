package dev.hilligans.ourcraft.Network.Packet.Server;

import dev.hilligans.ourcraft.ClientMain;
import dev.hilligans.ourcraft.Entity.Entity;
import dev.hilligans.ourcraft.Network.PacketBase;
import dev.hilligans.ourcraft.Network.PacketData;

public class SCreateEntityPacket extends PacketBase {

    int entityType;

    Entity entity;


    public SCreateEntityPacket() {
        super(9);
    }

    public SCreateEntityPacket(Entity entity) {
        this();
        this.entity = entity;
    }

    @Override
    public void encode(PacketData packetData) {
        entity.writeData(packetData);
    }

    @Override
    public void decode(PacketData packetData) {
       entityType = packetData.readInt();
       entity = Entity.entities.get(entityType).getEntity(packetData);
    }

    @Override
    public void handle() {
        ClientMain.getClient().clientWorld.addEntity(entity);
    }
}
