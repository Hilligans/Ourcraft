package Hilligans.Network.Packet.Server;

import Hilligans.Client.Rendering.Screens.DisconnectScreen;
import Hilligans.ClientMain;
import Hilligans.Network.PacketBase;
import Hilligans.Network.PacketData;

public class SDisconnectPacket extends PacketBase {

    public SDisconnectPacket() {
        super(26);
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
        ClientMain.openScreen(new DisconnectScreen(disconnectReason));
    }
}
