package dev.hilligans.ourcraft.Network.Packet.Server;

import dev.hilligans.ourcraft.Client.Rendering.Screens.DisconnectScreen;
import dev.hilligans.ourcraft.ClientMain;
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
