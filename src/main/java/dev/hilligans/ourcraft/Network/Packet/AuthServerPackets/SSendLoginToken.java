package dev.hilligans.ourcraft.Network.Packet.AuthServerPackets;

import dev.hilligans.ourcraft.Client.Client;
import dev.hilligans.ourcraft.ClientMain;
import dev.hilligans.ourcraft.Network.*;

public class SSendLoginToken extends PacketBaseNew<IClientPacketHandler> {

    String token;

    public SSendLoginToken() {
        super(3);
    }

    @Override
    public void encode(IPacketByteArray packetData) {}

    @Override
    public void decode(IPacketByteArray packetData) {
        token = packetData.readString();
    }

    @Override
    public void handle(IClientPacketHandler clientPacketHandler) {
        Client client = clientPacketHandler.getClient();
        client.playerData.login_token = token;
        client.saveUsernameAndPassword();
        client.authNetwork.sendPacket(new CGetToken(client.playerData.userName,token));
    }
}
