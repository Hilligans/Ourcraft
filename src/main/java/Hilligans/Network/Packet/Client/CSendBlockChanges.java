package Hilligans.Network.Packet.Client;

import Hilligans.Entity.Entities.ItemEntity;
import Hilligans.Entity.Entity;
import Hilligans.Network.Packet.Server.SCreateEntityPacket;
import Hilligans.Network.Packet.Server.SSendBlockChanges;
import Hilligans.Blocks.Blocks;
import Hilligans.Network.PacketBase;
import Hilligans.Network.PacketData;
import Hilligans.Network.ServerNetworkHandler;
import Hilligans.ServerMain;
import Hilligans.World.BlockState;

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
        ServerMain.world.setBlockState(x,y,z,new BlockState(Blocks.getBlockWithID(blockId)));
        if(oldState.block != Blocks.AIR && ServerMain.world.getBlockState(x,y,z).block == Blocks.AIR) {
            //ServerNetworkHandler.sendPacket(new SCreateEntityPacket(new ItemEntity(x,y,z, Entity.getNewId(), oldState.block)));
        }
        ServerNetworkHandler.sendPacket(new SSendBlockChanges(x,y,z,blockId));
    }
}
