package Hilligans.Network.Packet;

import Hilligans.Network.PacketBase;
import Hilligans.Network.PacketData;

public class InvalidFormatPacket extends PacketBase {

    public InvalidFormatPacket() {
        super(0);
    }

    @Override
    public void encode(PacketData packetData) { }

    @Override
    public void decode(PacketData packetData) {

    }

    @Override
    public void handle() {
        System.err.println("Received an invalid packet");
    }

    public PacketBase createNew() {
        return new InvalidFormatPacket();
    }
}
