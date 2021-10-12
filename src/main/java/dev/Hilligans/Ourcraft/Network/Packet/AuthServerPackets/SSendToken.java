package dev.Hilligans.Ourcraft.Network.Packet.AuthServerPackets;

import dev.Hilligans.Ourcraft.ClientMain;
import dev.Hilligans.Ourcraft.Network.PacketBase;
import dev.Hilligans.Ourcraft.Network.PacketData;

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
