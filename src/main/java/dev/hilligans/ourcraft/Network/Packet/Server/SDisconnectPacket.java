package dev.hilligans.ourcraft.Network.Packet.Server;

import dev.hilligans.ourcraft.Client.Rendering.Screens.DisconnectScreen;
import dev.hilligans.ourcraft.ClientMain;
import dev.hilligans.ourcraft.Network.*;

public class SDisconnectPacket extends PacketBaseNew<IClientPacketHandler> {

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
