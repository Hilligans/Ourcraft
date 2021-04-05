package Hilligans.Network.Packet.AuthServerPackets;

import Hilligans.Client.ClientPlayerData;
import Hilligans.Client.Rendering.Screens.AccountCreationScreen;
import Hilligans.ClientMain;
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
        if(!response.equals("Account created successfully")) {
            ClientPlayerData.valid_account = false;
        }
        if(ClientMain.screen instanceof AccountCreationScreen) {
            ((AccountCreationScreen) ClientMain.screen).debug = response;
        }
    }
}
