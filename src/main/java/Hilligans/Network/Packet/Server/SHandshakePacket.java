package Hilligans.Network.Packet.Server;

import Hilligans.ClientMain;
import Hilligans.Data.Other.ServerSidedData;
import Hilligans.Network.PacketBase;
import Hilligans.Network.PacketData;

public class SHandshakePacket extends PacketBase {

    public int playerId;
    public long dataVersion;

    public SHandshakePacket() {
        super(6);
    }

    public SHandshakePacket(int playerId, long dataVersion) {
        this();
        this.playerId = playerId;
        this.dataVersion = dataVersion;
    }

    @Override
    public void encode(PacketData packetData) {
        packetData.writeInt(playerId);
        packetData.writeLong(dataVersion);
    }

    @Override
    public void decode(PacketData packetData) {
        playerId = packetData.readInt();
        dataVersion = packetData.readLong();
    }

    @Override
    public void handle() {
        ClientMain.getClient().playerId = playerId;
        ClientMain.getClient().valid = true;
        ServerSidedData serverSidedData = ServerSidedData.getInstance();
        if(serverSidedData.version != dataVersion) {
            serverSidedData.clear();
            serverSidedData.version = dataVersion;
        }
    }
}
