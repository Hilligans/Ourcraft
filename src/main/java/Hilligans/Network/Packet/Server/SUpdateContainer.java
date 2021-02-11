package Hilligans.Network.Packet.Server;

import Hilligans.Client.ClientData;
import Hilligans.Item.ItemStack;
import Hilligans.Network.PacketBase;
import Hilligans.Network.PacketData;

public class SUpdateContainer extends PacketBase {

    byte slot;
    ItemStack itemStack;
    int containerId = 0;

    public SUpdateContainer() {
        super(14);
    }

    public SUpdateContainer(byte slot, ItemStack itemStack) {
        this();
        this.slot = slot;
        this.itemStack = itemStack;
    }

    @Override
    public void encode(PacketData packetData) {
        packetData.writeByte(slot);
        packetData.writeItemStack(itemStack);
        packetData.writeInt(containerId);
    }

    @Override
    public void decode(PacketData packetData) {
        slot = packetData.readByte();
        itemStack = packetData.readItemStack();
        containerId = packetData.readInt();
    }

    @Override
    public void handle() {
        if(ClientData.openContainer != null) {
            ClientData.openContainer.slots.get(slot).setContents(itemStack);
        }
    }
}
