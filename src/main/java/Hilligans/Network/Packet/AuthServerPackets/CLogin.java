package Hilligans.Network.Packet.AuthServerPackets;

import Hilligans.Network.PacketBase;
import Hilligans.Network.PacketData;

public class CLogin extends PacketBase {

    String username;
    String password;


    public CLogin() {
        super(3);
    }

    public CLogin(String username, String password) {
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
    public void decode(PacketData packetData) {

    }

    @Override
    public void handle() {

    }
}
