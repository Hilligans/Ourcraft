package dev.Hilligans.Network.Packet.AuthServerPackets;

import dev.Hilligans.ClientMain;
import dev.Hilligans.Network.PacketBase;
import dev.Hilligans.Network.PacketData;

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
