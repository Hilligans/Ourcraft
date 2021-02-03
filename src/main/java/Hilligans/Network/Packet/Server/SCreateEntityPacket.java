package Hilligans.Network.Packet.Server;

import Hilligans.ClientMain;
import Hilligans.Entity.Entity;
import Hilligans.Entity.LivingEntities.PlayerEntity;
import Hilligans.Network.PacketBase;
import Hilligans.Network.PacketData;

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
        ClientMain.clientWorld.addEntity(entity);
    }
}
