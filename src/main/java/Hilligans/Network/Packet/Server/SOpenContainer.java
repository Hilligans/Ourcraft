package Hilligans.Network.Packet.Server;

import Hilligans.ClientMain;
import Hilligans.Container.Container;
import Hilligans.Container.Slot;
import Hilligans.Network.PacketBase;
import Hilligans.Network.PacketData;

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
            container.slots.get(x).setContents(packetData.readItemStack());
        }
    }

    @Override
    public void handle() {
        if(container != null) {
            ClientMain.openScreen(container);
        }
    }
}
