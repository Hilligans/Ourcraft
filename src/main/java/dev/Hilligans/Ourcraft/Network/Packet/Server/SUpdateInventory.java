package dev.Hilligans.Ourcraft.Network.Packet.Server;

import dev.Hilligans.Ourcraft.ClientMain;
import dev.Hilligans.Ourcraft.Data.Other.Inventory;
import dev.Hilligans.Ourcraft.Network.PacketBase;
import dev.Hilligans.Ourcraft.Network.PacketData;

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
