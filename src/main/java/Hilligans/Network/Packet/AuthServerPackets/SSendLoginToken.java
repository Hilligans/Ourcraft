package Hilligans.Network.Packet.AuthServerPackets;

import Hilligans.ClientMain;
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
        ClientMain.getClient().saveUsernameAndPassword();
        ClientMain.getClient().authNetwork.sendPacket(new CGetToken(ClientMain.getClient().playerData.userName,token));
    }
}
