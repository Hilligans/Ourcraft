package dev.Hilligans.Ourcraft.Network.Packet.Client;

import dev.Hilligans.Ourcraft.Network.PacketBase;
import dev.Hilligans.Ourcraft.Network.PacketData;
import dev.Hilligans.Ourcraft.Network.ServerNetworkHandler;

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
    public void encode(PacketData packetData) {
        packetData.writeShort(slot);
        packetData.writeByte(count);
    }

    @Override
    public void decode(PacketData packetData) {
        slot = packetData.readShort();
        count = packetData.readByte();
    }

    @Override
    public void handle() {
        ServerNetworkHandler.getPlayerData(ctx).dropItem(slot,count);
    }
}
