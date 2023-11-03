package dev.hilligans.ourcraft.Network.Packet.Client;

import dev.hilligans.ourcraft.Data.Other.BlockStates.BlockState;
import dev.hilligans.ourcraft.Data.Other.BlockPos;
import dev.hilligans.ourcraft.Item.ItemStack;
import dev.hilligans.ourcraft.Network.IPacketByteArray;
import dev.hilligans.ourcraft.Network.PacketBase;
import dev.hilligans.ourcraft.Network.PacketData;
import dev.hilligans.ourcraft.Network.ServerNetworkHandler;
import dev.hilligans.ourcraft.Data.Other.Server.ServerPlayerData;
import dev.hilligans.ourcraft.ServerMain;

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
    public void encode(IPacketByteArray packetData) {
        packetData.writeByte(slot);
    }

    @Override
    public void decode(IPacketByteArray packetData) {
        slot = packetData.readByte();
    }

    @Override
    public void handle() {

        if(slot >= 0 && slot < 9) {
            int dim = ServerNetworkHandler.getPlayerData(ctx).getDimension();
            ServerPlayerData serverPlayerData = ServerNetworkHandler.getPlayerData(ctx);
            if(serverPlayerData != null) {
                BlockPos blockPos = ServerMain.getWorld(dim).traceBlockToBreak(serverPlayerData.playerEntity.getX(), serverPlayerData.playerEntity.getY() + serverPlayerData.playerEntity.boundingBox.eyeHeight, serverPlayerData.playerEntity.getZ(), serverPlayerData.playerEntity.pitch, serverPlayerData.playerEntity.yaw);
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
