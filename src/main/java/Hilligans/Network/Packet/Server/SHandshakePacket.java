package Hilligans.Network.Packet.Server;

import Hilligans.Client.ClientData;
import Hilligans.ClientMain;
import Hilligans.Network.PacketBase;
import Hilligans.Network.PacketData;

public class SHandshakePacket extends PacketBase {

    public int playerId;
    public int containerId;

    public SHandshakePacket() {
        super(6);
    }

    public SHandshakePacket(int playerId, int containerId) {
        this();
        this.playerId = playerId;
        this.containerId = containerId;
    }

    @Override
    public void encode(PacketData packetData) {
        packetData.writeInt(playerId);
        packetData.writeInt(containerId);
    }

    @Override
    public void decode(PacketData packetData) {
        playerId = packetData.readInt();
        containerId = packetData.readInt();
    }

    @Override
    public void handle() {
        ClientMain.playerId = playerId;
        ClientData.containerId = containerId;
        ClientMain.valid = true;
    }
}
