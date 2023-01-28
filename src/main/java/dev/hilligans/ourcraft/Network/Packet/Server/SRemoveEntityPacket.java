package dev.hilligans.ourcraft.Network.Packet.Server;

import dev.hilligans.ourcraft.ClientMain;
import dev.hilligans.ourcraft.Network.PacketBase;
import dev.hilligans.ourcraft.Network.PacketData;

public class SRemoveEntityPacket extends PacketBase {

    int id;

    public SRemoveEntityPacket() {
        super(10);
    }

    public SRemoveEntityPacket(int id) {
        this();
        this.id = id;
    }

    @Override
    public void encode(PacketData packetData) {
        packetData.writeInt(id);
    }

    @Override
    public void decode(PacketData packetData) {
        id = packetData.readInt();
    }

    @Override
    public void handle() {
        ClientMain.getClient().clientWorld.entities.remove(id);
    }
}
