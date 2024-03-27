package dev.hilligans.ourcraft.network.packet.server;

import dev.hilligans.ourcraft.data.other.Inventory;
import dev.hilligans.ourcraft.network.*;

public class SUpdateInventory extends PacketBase<IClientPacketHandler> {

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
    public void handle(IClientPacketHandler clientPacketHandler) {
        clientPacketHandler.getClient().playerData.inventory.readData(packetData);
    }
}
