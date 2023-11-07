package dev.hilligans.ourcraft.network.packet.client;

import dev.hilligans.ourcraft.block.Block;
import dev.hilligans.ourcraft.block.blockstate.IBlockState;
import dev.hilligans.ourcraft.data.other.BlockPos;
import dev.hilligans.ourcraft.entity.entities.ItemEntity;
import dev.hilligans.ourcraft.entity.Entity;
import dev.hilligans.ourcraft.block.Blocks;
import dev.hilligans.ourcraft.network.*;
import dev.hilligans.ourcraft.ServerMain;

public class CSendBlockChanges extends PacketBaseNew<IServerPacketHandler> {

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
    public void encode(IPacketByteArray packetData) {
        packetData.writeInt(x);
        packetData.writeInt(y);
        packetData.writeInt(z);
        packetData.writeInt(blockId);
    }

    @Override
    public void decode(IPacketByteArray packetData) {
        x = packetData.readInt();
        y = packetData.readInt();
        z = packetData.readInt();
        blockId = packetData.readInt();
    }

    @Override
    public void handle(IServerPacketHandler serverPacketHandler) {
        int dim = serverPacketHandler.getServerPlayerData().getDimension();
        IBlockState oldState = serverPacketHandler.getWorld().getBlockState(x,y,z);
        IBlockState newBlock = Blocks.getBlockWithID(blockId).getDefaultState1();
        serverPacketHandler.getWorld().setBlockState(x,y,z,newBlock);
        newBlock.getBlock().onPlace(ServerMain.getWorld(dim), new BlockPos(x,y,z));
        Block droppedBlock = oldState.getBlock().droppedBlock;
        if(droppedBlock != Blocks.AIR) {
            if (serverPacketHandler.getWorld().getBlockState(x, y, z).getBlock() == Blocks.AIR) {
                ItemEntity itemEntity = new ItemEntity(x + 0.5f, y + 1, z + 0.5f, Entity.getNewId(), droppedBlock);
                serverPacketHandler.getWorld().addEntity(itemEntity);
            }
        }
        //ServerNetworkHandler.sendPacket(new SSendBlockChanges(x,y,z,newBlock));
    }
}
