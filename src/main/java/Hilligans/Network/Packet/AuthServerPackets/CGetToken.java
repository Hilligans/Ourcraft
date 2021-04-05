package Hilligans.Network.Packet.AuthServerPackets;

import Hilligans.Network.PacketBase;
import Hilligans.Network.PacketData;

public class CGetToken extends PacketBase {

    String username;
    String password;

    public CGetToken() {
        super(1);
    }

    public CGetToken(String username, String password) {
        this();
        this.username = username;
        this.password = password;
    }

    @Override
    public void encode(PacketData packetData) {
        packetData.writeString(username);
        packetData.writeString(password);
    }

    @Override
    public void decode(PacketData packetData) {}

    @Override
    public void handle() {}
}
