package Hilligans.Network.Packet.AuthServerPackets;

import Hilligans.ClientMain;
import Hilligans.Network.ClientAuthNetworkHandler;
import Hilligans.Network.PacketBase;
import Hilligans.Network.PacketData;

public class SSendLoginToken extends PacketBase {

    String token;

    public SSendLoginToken() {
        super(3);
    }

    @Override
    public void encode(PacketData packetData) {}

    @Override
    public void decode(PacketData packetData) {
        token = packetData.readString();
    }

    @Override
    public void handle() {
        ClientMain.getClient().playerData.login_token = token;
        ClientAuthNetworkHandler.sendPacketDirect(new CGetToken(ClientMain.getClient().playerData.userName,token));
    }
}
