package Hilligans.Network.Packet.Server;

import Hilligans.ClientMain;
import Hilligans.Network.PacketBase;
import Hilligans.Network.PacketData;

public class SHandshakePacket extends PacketBase {

    public int playerId;

    public SHandshakePacket() {
        super(6);
    }

    public SHandshakePacket(int playerId) {
        this();
        this.playerId = playerId;
    }

    @Override
    public void encode(PacketData packetData) {
        packetData.writeInt(playerId);
    }

    @Override
    public void decode(PacketData packetData) {
        playerId = packetData.readInt();
    }

    @Override
    public void handle() {
        ClientMain.playerId = playerId;
        ClientMain.valid = true;
    }
}
