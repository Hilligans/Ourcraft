package Hilligans.Network.Packet.Server;

import Hilligans.Client.ClientPlayerData;
import Hilligans.Item.ItemStack;
import Hilligans.Network.PacketBase;
import Hilligans.Network.PacketData;

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
    public void encode(PacketData packetData) {
        packetData.writeBoolean(trackInt);
        if(trackInt) {
            packetData.writeShort(integerId);
            packetData.writeInt(val);
        } else {
            packetData.writeShort(slot);
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
            slot = packetData.readShort();
            itemStack = packetData.readItemStack();
        }
        containerId = packetData.readInt();
    }

    @Override
    public void handle() {
        if(ClientPlayerData.openContainer != null) {
            if(ClientPlayerData.openContainer.uniqueId == containerId) {
                if(trackInt) {
                    ClientPlayerData.openContainer.setInt(integerId,val);
                } else {
                    ClientPlayerData.openContainer.getSlot(slot).setContents(itemStack);
                }
            }
        }
    }
}
