package Hilligans.Network.Packet.Client;

import Hilligans.Item.ItemStack;
import Hilligans.Network.Packet.Server.SUpdateContainer;
import Hilligans.Network.PacketBase;
import Hilligans.Network.PacketData;
import Hilligans.Network.ServerNetworkHandler;
import Hilligans.ServerMain;

public class CUpdateContainer extends PacketBase {

    int id;
    byte slot;
    ItemStack itemStack;

    public CUpdateContainer() {
        super(15);
    }

    public CUpdateContainer(byte slot, ItemStack itemStack) {
        this();
        this.slot = slot;
        this.itemStack = itemStack;
    }

    @Override
    public void encode(PacketData packetData) {
        packetData.writeInt(id);
        packetData.writeByte(slot);
        packetData.writeItemStack(itemStack);
    }

    @Override
    public void decode(PacketData packetData) {
        id = packetData.readInt();
        slot = packetData.readByte();
        itemStack = packetData.readItemStack();
    }

    @Override
    public void handle() {
        ServerMain.world.containerInventories.get(id).setItem(slot,itemStack);
        ServerNetworkHandler.sendPacket(new SUpdateContainer(slot,itemStack));
    }
}
