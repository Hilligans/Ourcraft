package Hilligans.Network.Packet.Server;

import Hilligans.Data.Other.BlockState;
import Hilligans.Block.Blocks;
import Hilligans.ClientMain;
import Hilligans.Data.Other.BlockPos;
import Hilligans.Data.Other.DataBlockState;
import Hilligans.Network.PacketBase;
import Hilligans.Network.PacketData;
import Hilligans.World.ClientWorld;

public class SSendBlockChanges extends PacketBase {
    int x;
    int y;
    int z;
    int blockId;
    short blockData;

    public SSendBlockChanges() {
        super(4);
    }

    public SSendBlockChanges(int x, int y, int z, BlockState blockState) {
        this();
        this.x = x;
        this.y = y;
        this.z = z;
        this.blockId = blockState.getBlock().id;
        if(blockState.getBlock().hasBlockState()) {
            blockData = ((DataBlockState)blockState).readData();
        }
    }

    public SSendBlockChanges(BlockPos pos, BlockState blockState) {
        this();
        this.x = pos.x;
        this.y = pos.y;
        this.z = pos.z;
        this.blockId = blockState.getBlock().id;
        if(blockState.getBlock().hasBlockState()) {
            blockData = ((DataBlockState)blockState).readData();
        }
    }

    @Override
    public void encode(PacketData packetData) {
        packetData.writeInt(x);
        packetData.writeInt(y);
        packetData.writeInt(z);
        packetData.writeInt(blockId);
        packetData.writeShort(blockData);
    }

    @Override
    public void decode(PacketData packetData) {
        x = packetData.readInt();
        y = packetData.readInt();
        z = packetData.readInt();
        blockId = packetData.readInt();
        blockData = packetData.readShort();
    }


    @Override
    public void handle() {
        ClientMain.getClient().clientWorld.blockChanges.add(new ClientWorld.BlockChange(x,y,z, Blocks.getBlockWithID(blockId).getStateWithData(blockData)));
    }
}
