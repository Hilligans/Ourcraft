package dev.hilligans.ourcraft.network.packet.client;

import dev.hilligans.ourcraft.network.*;
import dev.hilligans.ourcraft.data.other.server.ServerPlayerData;

public class CModifyStack extends PacketBaseNew<IServerPacketHandler> {

    byte mode;
    short slot;

    public CModifyStack() {
        super(13);
    }

    public CModifyStack(short slot, byte mode) {
        this();
        this.slot = slot;
        this.mode = mode;
    }

    @Override
    public void encode(IPacketByteArray packetData) {
        packetData.writeShort(slot);
        packetData.writeByte(mode);
    }

    @Override
    public void decode(IPacketByteArray packetData) {
        slot = packetData.readShort();
        mode = packetData.readByte();
    }

    @Override
    public void handle(IServerPacketHandler serverPacketHandler) {
        ServerPlayerData serverPlayerData = serverPacketHandler.getServerPlayerData();
        if(serverPlayerData != null) {
            if (mode == 0) {
                serverPlayerData.swapStack(slot);
            } else if (mode == 1) {
                serverPlayerData.splitStack(slot);
            } else if (mode == 2) {
                serverPlayerData.putOne(slot);
            } else {
                if(serverPlayerData.isCreative) {
                    serverPlayerData.copyStack(slot);
                }
            }
        }

        //ServerMain.world.containerInventories.get(id).setItem(slot,itemStack);
        //ServerNetworkHandler.sendPacket(new SUpdateContainer(slot,itemStack));
    }
}
