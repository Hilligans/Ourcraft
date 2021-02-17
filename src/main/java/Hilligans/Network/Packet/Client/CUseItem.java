package Hilligans.Network.Packet.Client;

import Hilligans.Block.BlockState;
import Hilligans.Client.ClientData;
import Hilligans.Data.Other.BlockPos;
import Hilligans.Item.ItemStack;
import Hilligans.Network.ClientNetworkHandler;
import Hilligans.Network.PacketBase;
import Hilligans.Network.PacketData;
import Hilligans.Network.ServerNetworkHandler;
import Hilligans.Server.PlayerData;
import Hilligans.ServerMain;
import Hilligans.World.ServerWorld;

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
            PlayerData playerData = ServerNetworkHandler.getPlayerData(ctx);
            if(playerData != null) {
                BlockPos blockPos = ServerMain.world.traceBlockToBreak(playerData.playerEntity.x, playerData.playerEntity.y + playerData.playerEntity.boundingBox.eyeHeight, playerData.playerEntity.z, playerData.playerEntity.pitch, playerData.playerEntity.yaw);
                if (blockPos != null) {
                    BlockState blockState = ServerMain.world.getBlockState(blockPos);
                    if (blockState != null && blockState.block.activateBlock(ServerMain.world, playerData.playerEntity, blockPos)) {
                        return;
                    }
                    ItemStack itemStack = playerData.playerInventory.getItem(slot);
                    if (!itemStack.isEmpty()) {
                        if (itemStack.item.onActivate(ServerMain.world, playerData.playerEntity)) {
                            if (!playerData.isCreative) {
                                itemStack.removeCount(1);
                            }
                        }
                    }
                }
            }
        }
    }
}
