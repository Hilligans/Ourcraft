package dev.hilligans.ourcraft.network.packet.client;

import dev.hilligans.ourcraft.network.*;

public class CDropItem extends PacketBaseNew<IServerPacketHandler> {

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
    public void handle(IServerPacketHandler serverPacketHandler) {
        serverPacketHandler.getServerPlayerData().dropItem(slot,count);
    }
}
