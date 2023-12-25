package dev.hilligans.ourcraft.network.packet.server;

import dev.hilligans.ourcraft.container.Container;
import dev.hilligans.ourcraft.container.Slot;
import dev.hilligans.ourcraft.network.*;

public class SOpenContainer extends PacketBaseNew<IClientPacketHandler> {

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
    public void encode(IPacketByteArray packetData) {
        packetData.writeShort((short) container.type);
        packetData.writeInt(uniqueId);
        packetData.writeShort((short) container.slots.size());
        for(Slot slot : container.slots) {
           // packetData.writeItemStack(slot.getContents());
        }
    }

    @Override
    public void decode(IPacketByteArray packetData) {
        container = Container.getContainer(packetData.readShort());
        container.uniqueId = packetData.readInt();
        int slotCount = packetData.readShort();
        for(int x = 0; x < slotCount; x++) {
            //ItemStack itemStack = packetData.readItemStack();
          //  container.getSlot(x).setContents(itemStack);
        }
    }

    @Override
    public void handle(IClientPacketHandler clientPacketHandler) {
        if(container != null) {
            clientPacketHandler.getClient().openScreen(container);
        }
    }
}