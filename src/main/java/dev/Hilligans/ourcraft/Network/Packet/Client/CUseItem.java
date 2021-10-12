package dev.Hilligans.ourcraft.Network.Packet.Client;

import dev.Hilligans.ourcraft.Data.Other.BlockStates.BlockState;
import dev.Hilligans.ourcraft.Data.Other.BlockPos;
import dev.Hilligans.ourcraft.Item.ItemStack;
import dev.Hilligans.ourcraft.Network.PacketBase;
import dev.Hilligans.ourcraft.Network.PacketData;
import dev.Hilligans.ourcraft.Network.ServerNetworkHandler;
import dev.Hilligans.ourcraft.Data.Other.Server.ServerPlayerData;
import dev.Hilligans.ourcraft.ServerMain;

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
