package dev.hilligans.ourcraft.Network.Packet.AuthServerPackets;

import dev.hilligans.ourcraft.ClientMain;
import dev.hilligans.ourcraft.Network.*;

public class SSendToken extends PacketBaseNew<IClientPacketHandler> {

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
