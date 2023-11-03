package dev.hilligans.ourcraft.Network.Packet.AuthServerPackets;

import dev.hilligans.ourcraft.Network.IPacketByteArray;
import dev.hilligans.ourcraft.Network.PacketBase;
import dev.hilligans.ourcraft.Network.PacketData;

public class CGetToken extends PacketBase {

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
    public void handle() {}
}
