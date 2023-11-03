package dev.hilligans.ourcraft.Network.Packet.Client;

import dev.hilligans.ourcraft.Network.IPacketByteArray;
import dev.hilligans.ourcraft.Network.PacketBase;
import dev.hilligans.ourcraft.Network.PacketData;

public class CActivateBlock extends PacketBase {
    public CActivateBlock() {
        super(15);
    }

    @Override
    public void encode(IPacketByteArray packetData) {

    }

    @Override
    public void decode(IPacketByteArray packetData) {

    }

    @Override
    public void handle() {

    }
}
