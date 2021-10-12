package dev.Hilligans.Ourcraft.Network.Packet.AuthServerPackets;

import dev.Hilligans.Ourcraft.Client.Rendering.Screens.AccountCreationScreen;
import dev.Hilligans.Ourcraft.ClientMain;
import dev.Hilligans.Ourcraft.Network.PacketBase;
import dev.Hilligans.Ourcraft.Network.PacketData;

public class SAccountPacket extends PacketBase {

    String response;

    public SAccountPacket() {
        super(0);
    }

    @Override
    public void encode(PacketData packetData) {}

    @Override
    public void decode(PacketData packetData) {
        response = packetData.readString();
    }

    @Override
    public void handle() {
        System.out.println(response);
        if(ClientMain.getClient().screen instanceof AccountCreationScreen) {
            ((AccountCreationScreen) ClientMain.getClient().screen).debug = response;
        }
    }
}
