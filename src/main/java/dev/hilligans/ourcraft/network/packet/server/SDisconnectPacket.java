package dev.hilligans.ourcraft.network.packet.server;

import dev.hilligans.ourcraft.client.rendering.screens.DisconnectScreen;
import dev.hilligans.ourcraft.network.*;

public class SDisconnectPacket extends PacketBase<IClientPacketHandler> {

    public SDisconnectPacket() {
        super(22);
    }

    String disconnectReason;

    public SDisconnectPacket(String disconnectReason) {
        this();
        this.disconnectReason = disconnectReason;
    }

    @Override
    public void encode(IPacketByteArray packetData) {
        packetData.writeString(disconnectReason);
    }

    @Override
    public void decode(IPacketByteArray packetData) {
        disconnectReason = packetData.readString();
    }

    @Override
    public void handle(IClientPacketHandler clientPacketHandler) {
        clientPacketHandler.getClient().openScreen(new DisconnectScreen(clientPacketHandler.getClient(),disconnectReason));
    }
}
