package dev.hilligans.ourcraft.network.packet.auth;


import dev.hilligans.ourcraft.network.IPacketByteArray;
import dev.hilligans.ourcraft.network.IPacketHandler;
import dev.hilligans.ourcraft.network.PacketBase;


public class CCreateAccount extends PacketBase<IPacketHandler> {

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
    public void encode(IPacketByteArray packetData) {
        packetData.writeString(username);
        packetData.writeString(password);
        packetData.writeString(email);
        packetData.writeString(verification_token);
    }

    @Override
    public void decode(IPacketByteArray packetData) {}

    @Override
    public void handle(IPacketHandler iPacketHandler) {

    }
}
