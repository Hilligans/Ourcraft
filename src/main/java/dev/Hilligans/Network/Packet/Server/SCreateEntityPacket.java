package dev.Hilligans.Network.Packet.Server;

import dev.Hilligans.ClientMain;
import dev.Hilligans.Entity.Entity;
import dev.Hilligans.Network.PacketBase;
import dev.Hilligans.Network.PacketData;

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
