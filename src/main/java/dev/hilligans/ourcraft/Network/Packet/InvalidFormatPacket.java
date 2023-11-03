package dev.hilligans.ourcraft.Network.Packet;

import dev.hilligans.ourcraft.Network.IPacketByteArray;
import dev.hilligans.ourcraft.Network.PacketBase;
import dev.hilligans.ourcraft.Network.PacketData;

public class InvalidFormatPacket extends PacketBase {

    public InvalidFormatPacket() {
        super(0);
    }

    @Override
    public void encode(IPacketByteArray packetData) {}

    @Override
    public void decode(IPacketByteArray packetData) {}

    @Override
    public void handle() {
        System.err.println("Received an invalid packet");
    }
}
