package dev.hilligans.ourcraft.network.packet.client;

import dev.hilligans.ourcraft.data.other.BlockPos;
import dev.hilligans.ourcraft.network.*;
import dev.hilligans.ourcraft.data.other.server.ServerPlayerData;

public class CUseItem extends PacketBase<IServerPacketHandler> {

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
    public void handle(IServerPacketHandler serverPacketHandler) {

        if(slot >= 0 && slot < 9) {
            int dim = serverPacketHandler.getServerPlayerData().getDimension();
            ServerPlayerData serverPlayerData = serverPacketHandler.getServerPlayerData();
            if(serverPlayerData != null) {
                //todo fix
                BlockPos blockPos = null;
                //BlockPos blockPos = ServerMain.getWorld(dim).traceBlockToBreak(serverPlayerData.playerEntity.getX(), serverPlayerData.playerEntity.getY() + serverPlayerData.playerEntity.boundingBox.eyeHeight, serverPlayerData.playerEntity.getZ(), serverPlayerData.playerEntity.pitch, serverPlayerData.playerEntity.yaw);
                if (blockPos != null) {
                    /*
                    BlockState blockState = ServerMain.getWorld(dim).getBlockState(blockPos);
                    if (blockState != null && blockState.getBlock().activateBlock(serverPacketHandler.getWorld(), serverPlayerData.playerEntity, blockPos)) {
                        return;
                    }
                    ItemStack itemStack = serverPlayerData.playerInventory.getItem(slot);
                    if (!itemStack.isEmpty()) {
                        if (itemStack.item.onActivate(serverPacketHandler.getWorld(), serverPlayerData.playerEntity)) {
                            if (!serverPlayerData.isCreative) {
                                itemStack.removeCount(1);
                            }
                        }
                    }

                     */
                }
            }
        }
    }
}
