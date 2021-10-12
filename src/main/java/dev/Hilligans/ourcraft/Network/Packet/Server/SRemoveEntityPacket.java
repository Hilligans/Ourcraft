package dev.Hilligans.ourcraft.Network.Packet.Server;

import dev.Hilligans.ourcraft.ClientMain;
import dev.Hilligans.ourcraft.Network.PacketBase;
import dev.Hilligans.ourcraft.Network.PacketData;

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
