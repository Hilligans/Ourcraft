package dev.Hilligans.ourcraft.Network.Packet;

import dev.Hilligans.ourcraft.Network.PacketBase;
import dev.Hilligans.ourcraft.Network.PacketData;

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
