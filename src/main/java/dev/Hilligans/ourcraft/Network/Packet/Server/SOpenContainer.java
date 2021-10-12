package dev.Hilligans.ourcraft.Network.Packet.Server;

import dev.Hilligans.ourcraft.ClientMain;
import dev.Hilligans.ourcraft.Container.Container;
import dev.Hilligans.ourcraft.Container.Slot;
import dev.Hilligans.ourcraft.Item.ItemStack;
import dev.Hilligans.ourcraft.Network.PacketBase;
import dev.Hilligans.ourcraft.Network.PacketData;

public class SOpenContainer extends PacketBase {

    Container container;
    int uniqueId;

    public SOpenContainer() {
        super(16);
    }

    public SOpenContainer(Container container) {
        this();
        this.container = container;
        this.uniqueId = container.uniqueId;
    }

    @Override
    public void encode(PacketData packetData) {
        packetData.writeShort((short) container.type);
        packetData.writeInt(uniqueId);
        packetData.writeShort((short) container.slots.size());
        for(Slot slot : container.slots) {
            packetData.writeItemStack(slot.getContents());
        }
    }

    @Override
    public void decode(PacketData packetData) {
        container = Container.getContainer(packetData.readShort());
        container.uniqueId = packetData.readInt();
        int slotCount = packetData.readShort();
        for(int x = 0; x < slotCount; x++) {
            ItemStack itemStack = packetData.readItemStack();
            container.getSlot(x).setContents(itemStack);
        }
    }

    @Override
    public void handle() {
        if(container != null) {
            ClientMain.getClient().openScreen(container);
        }
    }
}
