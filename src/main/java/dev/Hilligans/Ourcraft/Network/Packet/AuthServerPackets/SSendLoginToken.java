package dev.Hilligans.Ourcraft.Network.Packet.AuthServerPackets;

import dev.Hilligans.Ourcraft.ClientMain;
import dev.Hilligans.Ourcraft.Network.PacketBase;
import dev.Hilligans.Ourcraft.Network.PacketData;

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
