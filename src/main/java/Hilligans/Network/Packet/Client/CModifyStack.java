package Hilligans.Network.Packet.Client;

import Hilligans.Network.PacketBase;
import Hilligans.Network.PacketData;
import Hilligans.Network.ServerNetworkHandler;
import Hilligans.Data.Other.Server.ServerPlayerData;

public class CModifyStack extends PacketBase {

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
    public void encode(PacketData packetData) {
        packetData.writeShort(slot);
        packetData.writeByte(mode);
    }

    @Override
    public void decode(PacketData packetData) {
        slot = packetData.readShort();
        mode = packetData.readByte();
    }

    @Override
    public void handle() {
        ServerPlayerData serverPlayerData = ServerNetworkHandler.playerData.get(ServerNetworkHandler.mappedId.get(ctx.channel().id()));
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
