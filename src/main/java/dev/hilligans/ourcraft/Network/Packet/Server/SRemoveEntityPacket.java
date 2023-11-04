package dev.hilligans.ourcraft.Network.Packet.Server;

import dev.hilligans.ourcraft.ClientMain;
import dev.hilligans.ourcraft.Network.*;

public class SRemoveEntityPacket extends PacketBaseNew<IClientPacketHandler> {

    int id;

    public SRemoveEntityPacket() {
        super(10);
    }

    public SRemoveEntityPacket(int id) {
        this();
        this.id = id;
    }

    @Override
    public void encode(IPacketByteArray packetData) {
        packetData.writeInt(id);
    }

    @Override
    public void decode(IPacketByteArray packetData) {
        id = packetData.readInt();
    }

    @Override
    public void handle(IClientPacketHandler clientPacketHandler) {
        clientPacketHandler.getClient().newClientWorld.removeEntity(id, 0);
    }
}
