package dev.hilligans.ourcraft.Network.Packet.AuthServerPackets;

import dev.hilligans.ourcraft.ClientMain;
import dev.hilligans.ourcraft.Network.IPacketByteArray;
import dev.hilligans.ourcraft.Network.PacketBase;
import dev.hilligans.ourcraft.Network.PacketData;

public class SSendToken extends PacketBase {

    String token;
    public SSendToken() {
        super(1);
    }

    @Override
    public void encode(IPacketByteArray packetData) {
    }

    @Override
    public void decode(IPacketByteArray packetData) {
        token = packetData.readString();
    }

    @Override
    public void handle() {
        ClientMain.getClient().playerData.valid_account = true;
        ClientMain.getClient().playerData.authToken = token;
    }
}
