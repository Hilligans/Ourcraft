package dev.hilligans.ourcraft.network.packet.server;

import dev.hilligans.ourcraft.data.other.blockstates.BlockState;
import dev.hilligans.ourcraft.block.Blocks;
import dev.hilligans.ourcraft.data.other.BlockPos;
import dev.hilligans.ourcraft.data.other.blockstates.DataBlockState;
import dev.hilligans.ourcraft.network.*;
import dev.hilligans.ourcraft.world.ClientWorld;

public class SSendBlockChanges extends PacketBaseNew<IClientPacketHandler> {
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
    public void encode(IPacketByteArray packetData) {
        packetData.writeInt(x);
        packetData.writeInt(y);
        packetData.writeInt(z);
        packetData.writeInt(blockId);
        packetData.writeShort(blockData);
    }

    @Override
    public void decode(IPacketByteArray packetData) {
        x = packetData.readInt();
        y = packetData.readInt();
        z = packetData.readInt();
        blockId = packetData.readInt();
        blockData = packetData.readShort();
    }


    @Override
    public void handle(IClientPacketHandler clientPacketHandler) {
        clientPacketHandler.getClient().clientWorld.blockChanges.add(new ClientWorld.BlockChange(x,y,z, Blocks.getBlockWithID(blockId).getStateWithData(blockData)));
    }
}
