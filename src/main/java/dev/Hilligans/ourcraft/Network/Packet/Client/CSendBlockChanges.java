package dev.Hilligans.ourcraft.Network.Packet.Client;

import dev.Hilligans.ourcraft.Block.Block;
import dev.Hilligans.ourcraft.Data.Other.BlockPos;
import dev.Hilligans.ourcraft.Entity.Entities.ItemEntity;
import dev.Hilligans.ourcraft.Entity.Entity;
import dev.Hilligans.ourcraft.Block.Blocks;
import dev.Hilligans.ourcraft.Network.PacketBase;
import dev.Hilligans.ourcraft.Network.PacketData;
import dev.Hilligans.ourcraft.Network.ServerNetworkHandler;
import dev.Hilligans.ourcraft.ServerMain;
import dev.Hilligans.ourcraft.Data.Other.BlockStates.BlockState;

public class CSendBlockChanges extends PacketBase {

    int x;
    int y;
    int z;
    int blockId;

    public CSendBlockChanges(int x, int y, int z, int id) {
        super(3);
        this.x = x;
        this.y = y;
        this.z = z;
        this.blockId = id;
    }

    public CSendBlockChanges() {
        this(0,0,0,0);
    }

    @Override
    public void encode(PacketData packetData) {
        packetData.writeInt(x);
        packetData.writeInt(y);
        packetData.writeInt(z);
        packetData.writeInt(blockId);
    }

    @Override
    public void decode(PacketData packetData) {
        x = packetData.readInt();
        y = packetData.readInt();
        z = packetData.readInt();
        blockId = packetData.readInt();
    }

    @Override
    public void handle() {
        int dim = ServerNetworkHandler.getPlayerData(ctx).getDimension();
        BlockState oldState = ServerMain.getWorld(dim).getBlockState(x,y,z);
        BlockState newBlock = Blocks.getBlockWithID(blockId).getDefaultState();
        ServerMain.getWorld(dim).setBlockState(x,y,z,newBlock);
        newBlock.getBlock().onPlace(ServerMain.getWorld(dim), new BlockPos(x,y,z));
        Block droppedBlock = oldState.getBlock().getDroppedBlock();
        if(droppedBlock != Blocks.AIR) {
            if (ServerMain.getWorld(dim).getBlockState(x, y, z).getBlock() == Blocks.AIR) {
                ItemEntity itemEntity = new ItemEntity(x + 0.5f, y + 1, z + 0.5f, Entity.getNewId(), droppedBlock);
                ServerMain.getWorld(dim).addEntity(itemEntity);
            }
        }
        //ServerNetworkHandler.sendPacket(new SSendBlockChanges(x,y,z,newBlock));
    }
}
