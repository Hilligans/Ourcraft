package dev.hilligans.ourcraft.network.packet.server;

import dev.hilligans.ourcraft.entity.Entity;
import dev.hilligans.ourcraft.network.*;

public class SCreateEntityPacket extends PacketBase<IClientPacketHandler> {

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
    public void encode(IPacketByteArray packetData) {
        entity.writeData(packetData);
    }

    @Override
    public void decode(IPacketByteArray packetData) {
       entityType = packetData.readInt();
       entity = Entity.entities.get(entityType).getEntity(packetData);
    }

    @Override
    public void handle(IClientPacketHandler clientPacketHandler) {
        clientPacketHandler.getClient().newClientWorld.addEntity(entity);
    }
}
