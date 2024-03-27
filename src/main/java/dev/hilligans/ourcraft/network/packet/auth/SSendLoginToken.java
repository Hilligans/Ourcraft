package dev.hilligans.ourcraft.network.packet.auth;

import dev.hilligans.ourcraft.client.Client;
import dev.hilligans.ourcraft.network.*;

public class SSendLoginToken extends PacketBase<IClientPacketHandler> {

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
