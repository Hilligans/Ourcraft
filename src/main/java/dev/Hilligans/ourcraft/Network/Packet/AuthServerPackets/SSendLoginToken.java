package dev.Hilligans.ourcraft.Network.Packet.AuthServerPackets;

import dev.Hilligans.ourcraft.ClientMain;
import dev.Hilligans.ourcraft.Network.PacketBase;
import dev.Hilligans.ourcraft.Network.PacketData;

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
