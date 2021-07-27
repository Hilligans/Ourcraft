package Hilligans.Network.Packet.Server;

import Hilligans.Block.Block;
import Hilligans.ClientMain;
import Hilligans.Data.Other.BlockStates.DataBlockState;
import Hilligans.Data.Primitives.DoubleTypeWrapper;
import Hilligans.Util.Settings;
import Hilligans.World.Chunk;
import Hilligans.Block.Blocks;
import Hilligans.Network.PacketBase;
import Hilligans.Network.PacketData;
import Hilligans.Data.Other.BlockStates.BlockState;
import it.unimi.dsi.fastutil.shorts.Short2ByteOpenHashMap;

import java.util.ArrayList;

public class SSendChunkPacket extends PacketBase {

    public byte mode;
    public Chunk chunk;

    public SSendChunkPacket() {
        super(2);
        mode = 2;
    }

    public SSendChunkPacket(Chunk chunk) {
        this();
        mode = 2;
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
                        packetData.writeShort(chunk.getBlockState(x, y, z).getBlock().id);
                    }
                }
            }
        } else if(mode == 1) {
            ArrayList<Byte> blocks = new ArrayList<>();
            Short2ByteOpenHashMap mappedBlocks = new Short2ByteOpenHashMap();
            ArrayList<Short> blockIds = new ArrayList<>();
            ArrayList<Short> blockData = new ArrayList<>();
            byte pointer = 0;
            for (int x = 0; x < 16; x++) {
                for (int y = 0; y < Settings.chunkHeight * 16; y++) {
                    for (int z = 0; z < 16; z++) {
                        BlockState blockState = chunk.getBlockState(x, y, z);
                        byte id = mappedBlocks.getOrDefault(blockState.getBlock().id,(byte)-1);
                        boolean hasData = blockState.getBlock().hasBlockState();
                        if(id == -1 || (hasData && blockData.get(id) != ((DataBlockState)blockState).readData())) {
                            mappedBlocks.put(blockState.getBlock().id,pointer);
                            blockIds.add(blockState.getBlock().id);
                            if(hasData) {
                                blockData.add(((DataBlockState) blockState).readData());
                            } else {
                                blockData.add((short) 0);
                            }
                            id = pointer;
                            pointer++;
                        }
                        blocks.add(id);
                    }
                }
            }
            int size = blockIds.size();
            packetData.writeByte((byte)size);

            for(int x = 0; x < size; x++) {
                packetData.writeShort(blockIds.get(x));
                packetData.writeShort(blockData.get(x));
            }

            for(Byte byteVal : blocks) {
                packetData.writeByte(byteVal);
            }

        } else if(mode == 2) {
            ArrayList<DoubleTypeWrapper<BlockState,Integer>> blockList = chunk.getBlockChainedList();
            packetData.writeInt(blockList.size());
            for(DoubleTypeWrapper<BlockState,Integer> block : blockList) {
                packetData.writeShort(block.getTypeA().blockId);
                if(block.getTypeA().getBlock().hasBlockState()) {
                    if(block.getTypeA() instanceof DataBlockState) {
                        packetData.writeShort(block.getTypeA().readData());
                    }
                }
                packetData.writeInt(block.getTypeB());
            }

        }
    }


    @Override
    public void decode(PacketData packetData) {
        try {
            mode = packetData.readByte();
            chunk = new Chunk(packetData.readInt(), packetData.readInt(), ClientMain.getClient().clientWorld);

            if (mode == 0) {
                for (int x = 0; x < 16; x++) {
                    for (int y = 0; y < Settings.chunkHeight * 16; y++) {
                        for (int z = 0; z < 16; z++) {
                            chunk.setBlockState(x, y, z, Blocks.getBlockWithID(packetData.readShort()).getDefaultState());
                        }
                    }
                }
            } else if (mode == 1) {

                int size = packetData.readByte();
                ArrayList<Short> blocks = new ArrayList<>();
                ArrayList<Short> blockData = new ArrayList<>();

                for (int x = 0; x < size; x++) {
                    blocks.add(packetData.readShort());
                    blockData.add(packetData.readShort());
                }
                for (int x = 0; x < 16; x++) {
                    for (int y = 0; y < Settings.chunkHeight * 16; y++) {
                        for (int z = 0; z < 16; z++) {
                            byte block = packetData.readByte();
                            chunk.setBlockState(x, y, z, Blocks.getBlockWithID(blocks.get(block)).getStateWithData(blockData.get(block)));
                        }
                    }
                }
            } else if(mode == 2) {
                ArrayList<DoubleTypeWrapper<BlockState, Integer>> blockList = new ArrayList<>();
                int length = packetData.readInt();
                for(int x = 0; x < length; x++) {
                    short blockID = packetData.readShort();
                    Block block = Blocks.getBlockWithID(blockID);
                    if(block.hasBlockState()) {
                        blockList.add(new DoubleTypeWrapper<>(block.getStateWithData(packetData.readShort()),packetData.readInt()));
                    } else {
                        blockList.add(new DoubleTypeWrapper<>(block.getDefaultState(),packetData.readInt()));
                    }
                }
                chunk.setFromChainedList(blockList);
            }
        } catch (Exception ignored) {}
    }

    @Override
    public void handle() {
        try {
            chunk.world.setChunk(chunk);
        } catch (Exception ignored) {}
    }
}
