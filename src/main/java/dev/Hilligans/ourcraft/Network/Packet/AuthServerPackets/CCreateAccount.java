package dev.Hilligans.ourcraft.Network.Packet.AuthServerPackets;


import dev.Hilligans.ourcraft.Network.PacketBase;
import dev.Hilligans.ourcraft.Network.PacketData;


public class CCreateAccount extends PacketBase {

    String username;
    String password;
    String email;
    String verification_token;

    public CCreateAccount() {
        super(0);
    }

    public CCreateAccount(String username, String password, String email, String verification_token) {
        this();
        this.username = username;
        this.password = password;
        this.email = email;
        this.verification_token = verification_token;

    }

    @Override
    public void encode(PacketData packetData) {
        packetData.writeString(username);
        packetData.writeString(password);
        packetData.writeString(email);
        packetData.writeString(verification_token);
    }

    @Override
    public void decode(PacketData packetData) {}

    @Override
    public void handle() {}


}
