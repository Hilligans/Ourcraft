package dev.hilligans.ourcraft.Network.Packet.Server;

import dev.hilligans.ourcraft.ClientMain;
import dev.hilligans.ourcraft.Item.ItemStack;
import dev.hilligans.ourcraft.Network.IPacketByteArray;
import dev.hilligans.ourcraft.Network.PacketBase;
import dev.hilligans.ourcraft.Network.PacketData;

public class SUpdateContainer extends PacketBase {

    short slot;
    ItemStack itemStack;
    int containerId = 0;
    boolean trackInt = false;

    public SUpdateContainer() {
        super(14);
    }

    public SUpdateContainer(short slot, ItemStack itemStack, int uniqueId) {
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
    public void encode(IPacketByteArray packetData) {
        packetData.writeBoolean(trackInt);
        if(trackInt) {
            packetData.writeShort(integerId);
            packetData.writeInt(val);
        } else {
            packetData.writeShort(slot);
          //  packetData.writeItemStack(itemStack);
        }
        packetData.writeInt(containerId);
    }

    @Override
    public void decode(IPacketByteArray packetData) {
        trackInt = packetData.readBoolean();
        if(trackInt) {
            integerId = packetData.readShort();
            val = packetData.readInt();
        } else {
            slot = packetData.readShort();
           // itemStack = packetData.readItemStack();
        }
        containerId = packetData.readInt();
    }

    @Override
    public void handle() {
        if(ClientMain.getClient().playerData.openContainer != null) {
            if(ClientMain.getClient().playerData.openContainer.uniqueId == containerId) {
                if(trackInt) {
                    ClientMain.getClient().playerData.openContainer.setInt(integerId,val);
                } else {
                    ClientMain.getClient().playerData.openContainer.getSlot(slot).setContents(itemStack);
                }
            }
        }
    }
}
