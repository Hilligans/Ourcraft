package dev.Hilligans.ourcraft.Network.Packet.Server;

import dev.Hilligans.ourcraft.ClientMain;
import dev.Hilligans.ourcraft.Data.Other.Inventory;
import dev.Hilligans.ourcraft.Network.PacketBase;
import dev.Hilligans.ourcraft.Network.PacketData;

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
        ClientMain.getClient().playerData.inventory.readData(packetData);
    }
}
