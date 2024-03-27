package dev.hilligans.ourcraft.network.packet.auth;

import dev.hilligans.ourcraft.network.IPacketByteArray;
import dev.hilligans.ourcraft.network.IPacketHandler;
import dev.hilligans.ourcraft.network.PacketBase;

public class CGetToken extends PacketBase<IPacketHandler> {

    String username;
    String loginToken;

    public CGetToken() {
        super(1);
    }

    public CGetToken(String username, String loginToken) {
        this();
        this.username = username;
        this.loginToken = loginToken;
    }

    @Override
    public void encode(IPacketByteArray packetData) {
        packetData.writeString(username);
        packetData.writeString(loginToken);
    }

    @Override
    public void decode(IPacketByteArray packetData) {}


    @Override
    public void handle(IPacketHandler iPacketHandler) {

    }
}
