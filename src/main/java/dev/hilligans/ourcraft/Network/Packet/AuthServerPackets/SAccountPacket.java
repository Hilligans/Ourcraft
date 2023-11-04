package dev.hilligans.ourcraft.Network.Packet.AuthServerPackets;

import dev.hilligans.ourcraft.Client.Rendering.Screens.AccountCreationScreen;
import dev.hilligans.ourcraft.ClientMain;
import dev.hilligans.ourcraft.Network.*;

public class SAccountPacket extends PacketBaseNew<IClientPacketHandler> {

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
    public void handle(IClientPacketHandler clientPacketHandler) {
        System.out.println(response);
        if(clientPacketHandler.getClient().screen instanceof AccountCreationScreen) {
            ((AccountCreationScreen) clientPacketHandler.getClient().screen).debug = response;
        }
    }
}
