package dev.hilligans.ourcraft.network.packet.auth;

import dev.hilligans.ourcraft.client.rendering.screens.AccountCreationScreen;
import dev.hilligans.ourcraft.network.*;

public class SAccountPacket extends PacketBase<IClientPacketHandler> {

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
