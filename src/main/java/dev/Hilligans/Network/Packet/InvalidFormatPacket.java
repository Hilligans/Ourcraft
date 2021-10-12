package dev.Hilligans.Network.Packet;

import dev.Hilligans.Network.PacketBase;
import dev.Hilligans.Network.PacketData;

public class InvalidFormatPacket extends PacketBase {

    public InvalidFormatPacket() {
        super(0);
    }

    @Override
    public void encode(PacketData packetData) {}

    @Override
    public void decode(PacketData packetData) {}

    @Override
    public void handle() {
        System.err.println("Received an invalid packet");
    }
}
