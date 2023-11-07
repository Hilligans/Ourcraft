package dev.hilligans.ourcraft.network.packet.auth;

import dev.hilligans.ourcraft.network.IPacketByteArray;
import dev.hilligans.ourcraft.network.PacketBase;

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
    public void encode(IPacketByteArray packetData) {
        packetData.writeUTF16(username);
        packetData.writeUTF16(token);
        packetData.writeUTF16(ip);
        packetData.writeUTF16(tempId);
    }

    @Override
    public void decode(IPacketByteArray packetData) {}

    @Override
    public void handle() {}
}
