package dev.hilligans.ourcraft.Network.Packet.Server;

import dev.hilligans.ourcraft.Client.Rendering.Screens.DisconnectScreen;
import dev.hilligans.ourcraft.ClientMain;
import dev.hilligans.ourcraft.Network.IPacketByteArray;
import dev.hilligans.ourcraft.Network.PacketBase;
import dev.hilligans.ourcraft.Network.PacketData;

public class SDisconnectPacket extends PacketBase {

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
    public void handle() {
        ClientMain.getClient().openScreen(new DisconnectScreen(ClientMain.getClient(),disconnectReason));
    }
}
