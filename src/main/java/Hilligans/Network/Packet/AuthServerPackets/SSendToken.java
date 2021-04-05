package Hilligans.Network.Packet.AuthServerPackets;

import Hilligans.Client.ClientPlayerData;
import Hilligans.Network.ClientAuthNetworkHandler;
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
        System.out.println(token);
        ClientPlayerData.authToken = token;
    }
}
