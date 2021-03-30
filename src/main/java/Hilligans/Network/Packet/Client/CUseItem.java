package Hilligans.Network.Packet.Client;

import Hilligans.Data.Other.BlockState;
import Hilligans.Data.Other.BlockPos;
import Hilligans.Item.ItemStack;
import Hilligans.Network.PacketBase;
import Hilligans.Network.PacketData;
import Hilligans.Network.ServerNetworkHandler;
import Hilligans.Data.Other.Server.ServerPlayerData;
import Hilligans.ServerMain;

public class CUseItem extends PacketBase {

    byte slot;

    public CUseItem() {
        super(19);
    }

    public CUseItem(byte slot) {
        this();
        this.slot = slot;
    }


    @Override
    public void encode(PacketData packetData) {
        packetData.writeByte(slot);
    }

    @Override
    public void decode(PacketData packetData) {
        slot = packetData.readByte();
    }

    @Override
    public void handle() {

        if(slot >= 0 && slot < 9) {
            int dim = ServerNetworkHandler.getPlayerData(ctx).getDimension();
            ServerPlayerData serverPlayerData = ServerNetworkHandler.getPlayerData(ctx);
            if(serverPlayerData != null) {
                BlockPos blockPos = ServerMain.getWorld(dim).traceBlockToBreak(serverPlayerData.playerEntity.x, serverPlayerData.playerEntity.y + serverPlayerData.playerEntity.boundingBox.eyeHeight, serverPlayerData.playerEntity.z, serverPlayerData.playerEntity.pitch, serverPlayerData.playerEntity.yaw);
                if (blockPos != null) {
                    BlockState blockState = ServerMain.getWorld(dim).getBlockState(blockPos);
                    if (blockState != null && blockState.getBlock().activateBlock(ServerMain.getWorld(dim), serverPlayerData.playerEntity, blockPos)) {
                        return;
                    }
                    ItemStack itemStack = serverPlayerData.playerInventory.getItem(slot);
                    if (!itemStack.isEmpty()) {
                        if (itemStack.item.onActivate(ServerMain.getWorld(dim), serverPlayerData.playerEntity)) {
                            if (!serverPlayerData.isCreative) {
                                itemStack.removeCount(1);
                            }
                        }
                    }
                }
            }
        }
    }
}
