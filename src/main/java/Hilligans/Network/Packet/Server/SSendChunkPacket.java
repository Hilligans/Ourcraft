package Hilligans.Network.Packet.Server;

import Hilligans.ClientMain;
import Hilligans.Util.Settings;
import Hilligans.World.Chunk;
import Hilligans.Block.Blocks;
import Hilligans.Network.PacketBase;
import Hilligans.Network.PacketData;
import Hilligans.Block.BlockState;
import it.unimi.dsi.fastutil.ints.Int2BooleanOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.shorts.Short2ByteOpenHashMap;

import java.util.ArrayList;

public class SSendChunkPacket extends PacketBase {

    public byte mode;
    public Chunk chunk;

    public SSendChunkPacket() {
        super(2);
        mode = 1;
    }

    public SSendChunkPacket(Chunk chunk) {
        this();
        mode = 1;
        this.chunk = chunk;
    }

    @Override
    public void encode(PacketData packetData) {
        packetData.writeByte(mode);
        packetData.writeInt(chunk.x);
        packetData.writeInt(chunk.z);

        if(mode == 0) {
            for (int x = 0; x < 16; x++) {
                for (int y = 0; y < Settings.chunkHeight * 16; y++) {
                    for (int z = 0; z < 16; z++) {
                        packetData.writeShort(chunk.getBlockState(x, y, z).block.id);
                    }
                }
            }
        } else if(mode == 1) {
            ArrayList<Byte> blocks = new ArrayList<>();
            Short2ByteOpenHashMap mappedBlocks = new Short2ByteOpenHashMap();
            ArrayList<Short> blockIds = new ArrayList<>();
            byte pointer = 0;
            for (int x = 0; x < 16; x++) {
                for (int y = 0; y < Settings.chunkHeight * 16; y++) {
                    for (int z = 0; z < 16; z++) {
                        short blockId = chunk.getBlockState(x, y, z).block.id;
                        byte id = mappedBlocks.getOrDefault(blockId,(byte)-1);
                        if(id == -1) {
                            mappedBlocks.put(blockId,pointer);
                            blockIds.add(blockId);
                            id = pointer;
                            pointer++;
                        }
                        blocks.add(id);
                    }
                }
            }
            int size = blockIds.size();
            packetData.writeByte((byte)size);

            for(Short shortVal : blockIds) {
                packetData.writeShort(shortVal);
               // packetData.writeByte(mappedBlocks.get(shortVal));
            }

            for(Byte byteVal : blocks) {
                packetData.writeByte(byteVal);
            }

        }
    }

    @Override
    public void decode(PacketData packetData) {
        mode = packetData.readByte();
        chunk = new Chunk(packetData.readInt(),packetData.readInt(), ClientMain.clientWorld);

        if(mode == 0) {
            for (int x = 0; x < 16; x++) {
                for (int y = 0; y < Settings.chunkHeight * 16; y++) {
                    for (int z = 0; z < 16; z++) {
                        chunk.setBlockState(x, y, z, new BlockState(Blocks.getBlockWithID(packetData.readShort())));
                    }
                }
            }
        } else if(mode == 1) {

            int size = packetData.readByte();
            ArrayList<Short> blocks = new ArrayList<>();

            for(int x = 0; x < size; x++) {
                blocks.add(packetData.readShort());
            }
            for (int x = 0; x < 16; x++) {
                for (int y = 0; y < Settings.chunkHeight * 16; y++) {
                    for (int z = 0; z < 16; z++) {
                        chunk.setBlockState(x, y, z, new BlockState(Blocks.getBlockWithID(blocks.get(packetData.readByte()))));
                    }
                }
            }
        }
    }

    @Override
    public void handle() {
        chunk.world.setChunk(chunk);
    }
}
