package Hilligans.Network.Packet.Server;

import Hilligans.Container.Container;
import Hilligans.Container.Containers.ContainerBuilder;
import Hilligans.Container.Slot;
import Hilligans.Data.Other.IInventory;
import Hilligans.Data.Other.Inventory;
import Hilligans.Network.PacketBase;
import Hilligans.Network.PacketData;

public class SRegisterContainer extends PacketBase {

    Container container;
    ContainerBuilder containerBuilder;
    String textureName;

    public SRegisterContainer() {
        super(24);
    }

    public SRegisterContainer(Container container, String textureName) {
        this();
        this.container = container;
        this.textureName = textureName;
    }

    @Override
    public void encode(PacketData packetData) {
        packetData.writeShort((short) container.type);
        packetData.writeShort((short) container.slots.size());
        for(Slot slot : container.slots) {
            packetData.writeShort((short) slot.x);
            packetData.writeShort((short) slot.y);
            packetData.writeShort((short) slot.index);
        }
        packetData.writeString(textureName);
    }

    @Override
    public void decode(PacketData packetData) {
        short type = packetData.readShort();
        Slot[] slots = new Slot[packetData.readShort()];
        for(int x = 0; x < slots.length; x++) {
            slots[x] = new Slot(packetData.readShort(),packetData.readShort(),null,packetData.readShort());
        }
        containerBuilder = new ContainerBuilder(type,packetData.readString(),slots);
    }

    @Override
    public void handle() {
        Container.serverSideContainer.add(containerBuilder);
    }
}
