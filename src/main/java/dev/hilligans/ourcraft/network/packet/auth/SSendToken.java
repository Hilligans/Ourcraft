package dev.hilligans.ourcraft.network.packet.auth;

import dev.hilligans.ourcraft.network.*;

public class SSendToken extends PacketBase<IClientPacketHandler> {

    String token;
    public SSendToken() {
        super(1);
    }

    @Override
    public void encode(IPacketByteArray packetData) {
    }

    @Override
    public void decode(IPacketByteArray packetData) {
        token = packetData.readString();
    }

    @Override
    public void handle(IClientPacketHandler clientPacketHandler) {
        clientPacketHandler.getClient().playerData.valid_account = true;
        clientPacketHandler.getClient().playerData.authToken = token;
    }
}
