package Hilligans.Network.Packet.Server;

import Hilligans.ClientMain;
import Hilligans.Network.PacketBase;
import Hilligans.Network.PacketData;

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
