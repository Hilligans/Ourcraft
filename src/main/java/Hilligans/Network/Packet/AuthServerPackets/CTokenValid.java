package Hilligans.Network.Packet.AuthServerPackets;

import Hilligans.Network.PacketBase;
import Hilligans.Network.PacketData;

public class CTokenValid extends PacketBase {

    String username;
    String token;
    String ip;
    String tempId;

    public CTokenValid() {
        super(2);
    }

    public CTokenValid(String username, String token, String ip, String tempId) {
        super(2);
        this.username = username;
        this.token = token;
        this.ip = ip;
        this.tempId = tempId;
    }

    @Override
    public void encode(PacketData packetData) {
        packetData.writeString(username);
        packetData.writeString(token);
        packetData.writeString(ip);
        packetData.writeString(tempId);
    }

    @Override
    public void decode(PacketData packetData) {}

    @Override
    public void handle() {}
}
