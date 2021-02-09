package Hilligans.Network.Packet.Server;

import Hilligans.Client.ClientData;
import Hilligans.Data.Other.Inventory;
import Hilligans.Network.PacketBase;
import Hilligans.Network.PacketData;

public class SUpdateInventory extends PacketBase {

    Inventory inventory;

    PacketData packetData;

    public SUpdateInventory() {
        super(17);
    }

    public SUpdateInventory(Inventory inventory) {
        this();
        this.inventory = inventory;
    }

    @Override
    public void encode(PacketData packetData) {
        inventory.writeData(packetData);
    }

    @Override
    public void decode(PacketData packetData) {
        this.packetData = packetData;
    }

    @Override
    public void handle() {
        ClientData.inventory.readData(packetData);
    }
}
