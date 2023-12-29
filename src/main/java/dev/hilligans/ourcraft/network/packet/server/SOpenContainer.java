package dev.hilligans.ourcraft.network.packet.server;

import dev.hilligans.ourcraft.container.Container;
import dev.hilligans.ourcraft.container.Slot;
import dev.hilligans.ourcraft.network.*;

public class SOpenContainer extends PacketBaseNew<IClientPacketHandler> {


    public short containerID;
    public int uniqueID;
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
        containerID = packetData.readShort();
       // container = Container.getContainer(packetData.readShort(), packetData.);
        uniqueId = packetData.readInt();
        int slotCount = packetData.readShort();
        for(int x = 0; x < slotCount; x++) {
            //ItemStack itemStack = packetData.readItemStack();
          //  container.getSlot(x).setContents(itemStack);
        }
    }

    @Override
    public void handle(IClientPacketHandler clientPacketHandler) {
        container = Container.getContainer(containerID, clientPacketHandler.getClient());
        if(container != null) {
            container.uniqueId = uniqueId;
            clientPacketHandler.getClient().openScreen(container);
        }
    }
}
