package dev.hilligans.ourcraft.Network.Packet.Client;

import dev.hilligans.ourcraft.Network.IPacketByteArray;
import dev.hilligans.ourcraft.Network.PacketBase;
import dev.hilligans.ourcraft.Network.PacketData;
import dev.hilligans.ourcraft.Network.ServerNetworkHandler;

public class CDropItem extends PacketBase {

    short slot;
    byte count;

    public CDropItem() {
        super(18);
    }

    public CDropItem(short slot, byte count) {
        this();
        this.slot = slot;
        this.count = count;
    }

    @Override
    public void encode(IPacketByteArray packetData) {
        packetData.writeShort(slot);
        packetData.writeByte(count);
    }

    @Override
    public void decode(IPacketByteArray packetData) {
        slot = packetData.readShort();
        count = packetData.readByte();
    }

    @Override
    public void handle() {
        ServerNetworkHandler.getPlayerData(ctx).dropItem(slot,count);
    }
}
