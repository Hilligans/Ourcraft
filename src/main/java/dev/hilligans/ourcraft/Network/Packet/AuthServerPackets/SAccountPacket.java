package dev.hilligans.ourcraft.Network.Packet.AuthServerPackets;

import dev.hilligans.ourcraft.Client.Rendering.Screens.AccountCreationScreen;
import dev.hilligans.ourcraft.ClientMain;
import dev.hilligans.ourcraft.Network.IPacketByteArray;
import dev.hilligans.ourcraft.Network.PacketBase;
import dev.hilligans.ourcraft.Network.PacketData;

public class SAccountPacket extends PacketBase {

    String response;

    public SAccountPacket() {
        super(0);
    }

    @Override
    public void encode(IPacketByteArray packetData) {}

    @Override
    public void decode(IPacketByteArray packetData) {
        response = packetData.readUTF16();
    }

    @Override
    public void handle() {
        System.out.println(response);
        if(ClientMain.getClient().screen instanceof AccountCreationScreen) {
            ((AccountCreationScreen) ClientMain.getClient().screen).debug = response;
        }
    }
}
