package Hilligans.Network.Packet.AuthServerPackets;

import Hilligans.ClientMain;
import Hilligans.Network.PacketBase;
import Hilligans.Network.PacketData;

public class SSendToken extends PacketBase {

    String token;
    public SSendToken() {
        super(1);
    }

    @Override
    public void encode(PacketData packetData) {
    }

    @Override
    public void decode(PacketData packetData) {
        token = packetData.readString();
    }

    @Override
    public void handle() {
        ClientMain.getClient().playerData.valid_account = true;
        ClientMain.getClient().playerData.authToken = token;
    }
}
