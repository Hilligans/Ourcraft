package Hilligans.Network.Packet.Client;

import Hilligans.Network.PacketBase;
import Hilligans.Network.PacketData;
import Hilligans.Network.ServerNetworkHandler;

public class CDropItem extends PacketBase {

    short slot;

    public CDropItem() {
        super(18);
    }

    public CDropItem(short slot) {
        this();
        this.slot = slot;
    }

    @Override
    public void encode(PacketData packetData) {
        packetData.writeShort(slot);
    }

    @Override
    public void decode(PacketData packetData) {
        slot = packetData.readShort();
    }

    @Override
    public void handle() {
        ServerNetworkHandler.getPlayerData(ctx).dropItem(slot);
    }
}
