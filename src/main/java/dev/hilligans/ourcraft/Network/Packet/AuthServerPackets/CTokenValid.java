package dev.hilligans.ourcraft.Network.Packet.AuthServerPackets;

import dev.hilligans.ourcraft.Network.IPacketByteArray;
import dev.hilligans.ourcraft.Network.PacketBase;
import dev.hilligans.ourcraft.Network.PacketData;

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
