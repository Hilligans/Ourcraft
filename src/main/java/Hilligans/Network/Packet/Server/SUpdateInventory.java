package Hilligans.Network.Packet.Server;

import Hilligans.Network.PacketBase;
import Hilligans.Network.PacketData;

public class SUpdateInventory extends PacketBase {

    int age;


    public SUpdateInventory() {
        super(19);
    }

    @Override
    public void encode(PacketData packetData) {

    }

    @Override
    public void decode(PacketData packetData) {

    }

    @Override
    public void handle() {

    }
}
