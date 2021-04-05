package Hilligans.Network.Packet.AuthServerPackets;

import Hilligans.Network.PacketBase;
import Hilligans.Network.PacketData;

public class STokenValid extends PacketBase {

    String username;
    String uuid;
    boolean valid;

    public STokenValid() {
        super(2);
    }

    @Override
    public void encode(PacketData packetData) {}

    @Override
    public void decode(PacketData packetData) {
        username = packetData.readString();
        uuid = packetData.readString();
        valid = packetData.readBoolean();
    }

    @Override
    public void handle() {}
}
