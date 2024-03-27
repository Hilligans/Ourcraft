package dev.hilligans.ourcraft.network.packet;

import dev.hilligans.ourcraft.network.IPacketByteArray;
import dev.hilligans.ourcraft.network.IPacketHandler;
import dev.hilligans.ourcraft.network.PacketBase;

public class InvalidFormatPacket extends PacketBase<IPacketHandler> {

    public InvalidFormatPacket() {
        super(0);
    }

    @Override
    public void encode(IPacketByteArray packetData) {}

    @Override
    public void decode(IPacketByteArray packetData) {}

    @Override
    public void handle(IPacketHandler iPacketHandler) {
        System.err.println("Received an invalid packet");
    }
}
