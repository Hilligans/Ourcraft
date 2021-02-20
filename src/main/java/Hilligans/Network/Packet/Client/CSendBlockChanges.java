package Hilligans.Network.Packet.Client;

import Hilligans.Block.Block;
import Hilligans.Data.Other.BlockPos;
import Hilligans.Entity.Entities.ItemEntity;
import Hilligans.Entity.Entity;
import Hilligans.Network.Packet.Server.SSendBlockChanges;
import Hilligans.Block.Blocks;
import Hilligans.Network.PacketBase;
import Hilligans.Network.PacketData;
import Hilligans.Network.ServerNetworkHandler;
import Hilligans.ServerMain;
import Hilligans.Data.Other.BlockState;

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
        BlockState oldState = ServerMain.world.getBlockState(x,y,z);
        BlockState newBlock = Blocks.getBlockWithID(blockId).getDefaultState();
        ServerMain.world.setBlockState(x,y,z,newBlock);
        newBlock.getBlock().onPlace(ServerMain.world, new BlockPos(x,y,z));
        Block droppedBlock = oldState.getBlock().getDroppedBlock();
        if(droppedBlock != Blocks.AIR) {
            if (ServerMain.world.getBlockState(x, y, z).getBlock() == Blocks.AIR) {
                ItemEntity itemEntity = new ItemEntity(x + 0.5f, y + 1, z + 0.5f, Entity.getNewId(), droppedBlock);
                ServerMain.world.addEntity(itemEntity);
            }
        }
        ServerNetworkHandler.sendPacket(new SSendBlockChanges(x,y,z,newBlock));
    }
}
