package dev.Hilligans.Network.Packet.Server;

import dev.Hilligans.Client.Rendering.Screens.DisconnectScreen;
import dev.Hilligans.ClientMain;
import dev.Hilligans.Network.PacketBase;
import dev.Hilligans.Network.PacketData;

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
    public void encode(PacketData packetData) {
        packetData.writeString(disconnectReason);
    }

    @Override
    public void decode(PacketData packetData) {
        disconnectReason = packetData.readString();
    }

    @Override
    public void handle() {
        ClientMain.getClient().openScreen(new DisconnectScreen(ClientMain.getClient(),disconnectReason));
    }
}
