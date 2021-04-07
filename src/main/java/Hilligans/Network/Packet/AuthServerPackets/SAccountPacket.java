package Hilligans.Network.Packet.AuthServerPackets;

import Hilligans.Client.Client;
import Hilligans.Client.ClientPlayerData;
import Hilligans.Client.Rendering.Screens.AccountCreationScreen;
import Hilligans.ClientMain;
import Hilligans.Network.ClientAuthNetworkHandler;
import Hilligans.Network.PacketBase;
import Hilligans.Network.PacketData;

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
