package dev.hilligans.ourcraft.Network.Packet.Server;

import dev.hilligans.ourcraft.ClientMain;
import dev.hilligans.ourcraft.Container.Container;
import dev.hilligans.ourcraft.Container.Slot;
import dev.hilligans.ourcraft.Item.ItemStack;
import dev.hilligans.ourcraft.Network.PacketBase;
import dev.hilligans.ourcraft.Network.PacketData;

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
           // packetData.writeItemStack(slot.getContents());
        }
    }

    @Override
    public void decode(PacketData packetData) {
        container = Container.getContainer(packetData.readShort());
        container.uniqueId = packetData.readInt();
        int slotCount = packetData.readShort();
        for(int x = 0; x < slotCount; x++) {
            //ItemStack itemStack = packetData.readItemStack();
          //  container.getSlot(x).setContents(itemStack);
        }
    }

    @Override
    public void handle() {
        if(container != null) {
            ClientMain.getClient().openScreen(container);
        }
    }
}
