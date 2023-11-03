package dev.hilligans.ourcraft.Network.Packet.Server;

import dev.hilligans.ourcraft.ClientMain;
import dev.hilligans.ourcraft.Data.Other.Inventory;
import dev.hilligans.ourcraft.Network.IPacketByteArray;
import dev.hilligans.ourcraft.Network.PacketBase;
import dev.hilligans.ourcraft.Network.PacketData;

public class SUpdateInventory extends PacketBase {

    Inventory inventory;

    IPacketByteArray packetData;

    public SUpdateInventory() {
        super(17);
    }

    public SUpdateInventory(Inventory inventory) {
        this();
        this.inventory = inventory;
    }

    @Override
    public void encode(IPacketByteArray packetData) {
        inventory.writeData(packetData);
    }

    @Override
    public void decode(IPacketByteArray packetData) {
        this.packetData = packetData;
    }

    @Override
    public void handle() {
        ClientMain.getClient().playerData.inventory.readData(packetData);
    }
}
