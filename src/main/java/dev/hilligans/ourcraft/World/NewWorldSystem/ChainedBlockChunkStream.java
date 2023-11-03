package dev.hilligans.ourcraft.World.NewWorldSystem;

import dev.hilligans.ourcraft.Block.BlockState.IBlockState;
import dev.hilligans.ourcraft.Util.IByteArray;
import dev.hilligans.ourcraft.Network.PacketByteArray;
import io.netty.buffer.ByteBuf;

public class ChainedBlockChunkStream extends ChunkStream {


    public ChainedBlockChunkStream(String name) {
        super(name);
    }

    @Override
    public IChunk fillChunk(ByteBuf buffer, int position, IChunk chunk) {
        PacketByteArray array = new PacketByteArray(buffer);
        array.setReaderIndex(position);
        long xx = array.readVarInt();
        long yy = array.readVarInt();
        long zz = array.readVarInt();

        int width = array.readVarInt();
        int height = array.readVarInt();

        chunk.setChunkPosition(xx / width, yy / height, zz / width);

        int count = 0;

        int length = 0;
        int blockstateID = 0;
        IBlockState state = null;

        for(int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                for(int z = 0; z < width; z++) {
                    if (count >= length) {
                        count = 0;
                        length = array.readVarInt();
                        blockstateID = array.readVarInt();
                        state = gameInstance.BLOCK_STATES.get(blockstateID);
                        //System.out.println(state.getBlock().getName());
                    }
                    chunk.setBlockState(x, y, z, state);
                    count++;
                }
            }
        }
        return chunk;
    }

    @Override
    public int fillBuffer(ByteBuf buffer, int position, IChunk chunk) {
        IByteArray array = new PacketByteArray(buffer);
        array.setReaderIndex(position);
      //  array.size = position;
       // array.byteBuf.resetReaderIndex();
       // array.byteBuf.resetWriterIndex();
        array.writeVarInt((int) chunk.getBlockX());
        array.writeVarInt((int) chunk.getBlockY());
        array.writeVarInt((int) chunk.getBlockZ());

        array.writeVarInt(chunk.getWidth());
        array.writeVarInt(chunk.getHeight());

        int length = 0;
        int blockState = chunk.getBlockState1(0,0,0).getBlockStateID();

        for(int x = 0; x < chunk.getWidth(); x++) {
            for(int y = 0; y < chunk.getHeight(); y++) {
                for(int z = 0; z < chunk.getWidth(); z++) {
                    int newID = chunk.getBlockState1(x,y,z).getBlockStateID();
                    if(blockState != newID) {
                        array.writeVarInt(length);
                        array.writeVarInt(blockState);
                        blockState = newID;
                        length = 0;
                    }
                    length++;
                }
            }
        }
        array.writeVarInt(length);
        array.writeVarInt(blockState);
        chunk.setDirty(true);
        return (int) array.length();
    }
}
