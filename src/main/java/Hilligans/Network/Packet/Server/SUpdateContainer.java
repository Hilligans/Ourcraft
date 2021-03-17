package Hilligans.Network.Packet.Server;

import Hilligans.Client.ClientData;
import Hilligans.Item.ItemStack;
import Hilligans.Network.PacketBase;
import Hilligans.Network.PacketData;

public class SUpdateContainer extends PacketBase {

    byte slot;
    ItemStack itemStack;
    int containerId = 0;
    boolean trackInt = false;

    public SUpdateContainer() {
        super(14);
    }

    public SUpdateContainer(byte slot, ItemStack itemStack, int uniqueId) {
        this();
        this.slot = slot;
        this.itemStack = itemStack;
        this.containerId = uniqueId;
    }

    short integerId;
    int val;

    public SUpdateContainer(short integerId, int val, int containerId) {
        this();
        this.integerId = integerId;
        this.val = val;
        this.containerId = containerId;
        trackInt = true;
    }

    @Override
    public void encode(PacketData packetData) {
        packetData.writeBoolean(trackInt);
        if(trackInt) {
            packetData.writeShort(integerId);
            packetData.writeInt(val);
        } else {
            packetData.writeByte(slot);
            packetData.writeItemStack(itemStack);
        }
        packetData.writeInt(containerId);
    }

    @Override
    public void decode(PacketData packetData) {
        trackInt = packetData.readBoolean();
        if(trackInt) {
            integerId = packetData.readShort();
            val = packetData.readInt();
        } else {
            slot = packetData.readByte();
            itemStack = packetData.readItemStack();
        }
        containerId = packetData.readInt();
    }

    @Override
    public void handle() {
        if(ClientData.openContainer != null) {
            if(ClientData.openContainer.uniqueId == containerId) {
                if(trackInt) {
                    ClientData.openContainer.setInt(integerId,val);
                } else {
                    ClientData.openContainer.slots.get(slot).setContents(itemStack);
                }
            }
        }
    }
}
