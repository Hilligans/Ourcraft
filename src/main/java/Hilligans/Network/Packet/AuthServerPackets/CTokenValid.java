package Hilligans.Network.Packet.AuthServerPackets;

import Hilligans.Network.PacketBase;
import Hilligans.Network.PacketData;

public class CTokenValid extends PacketBase {

    String username;
    String token;
    String ip;

    public CTokenValid(String username, String token, String ip) {
        super(2);
        this.username = username;
        this.token = token;
        this.ip = ip;
    }

    @Override
    public void encode(PacketData packetData) {
        packetData.writeString(username);
        packetData.writeString(token);
        packetData.writeString(ip);
    }

    @Override
    public void decode(PacketData packetData) {}

    @Override
    public void handle() {}
}
