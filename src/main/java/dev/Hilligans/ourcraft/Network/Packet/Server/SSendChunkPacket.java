package dev.Hilligans.ourcraft.Network.Packet.Server;

import dev.Hilligans.ourcraft.Block.Block;
import dev.Hilligans.ourcraft.ClientMain;
import dev.Hilligans.ourcraft.Data.Other.BlockStates.DataBlockState;
import dev.Hilligans.ourcraft.Data.Primitives.Tuple;
import dev.Hilligans.ourcraft.Util.Settings;
import dev.Hilligans.ourcraft.World.Chunk;
import dev.Hilligans.ourcraft.Block.Blocks;
import dev.Hilligans.ourcraft.Network.PacketBase;
import dev.Hilligans.ourcraft.Network.PacketData;
import dev.Hilligans.ourcraft.Data.Other.BlockStates.BlockState;
import dev.Hilligans.ourcraft.World.NewWorldSystem.ClassicChunk;
import dev.Hilligans.ourcraft.World.NewWorldSystem.IChunk;
import it.unimi.dsi.fastutil.shorts.Short2ByteOpenHashMap;

import java.util.ArrayList;

public class SSendChunkPacket extends PacketBase {

    public byte mode;
    public Chunk chunk;

    public SSendChunkPacket() {
        super(2);
        mode = 0;
    }

    public SSendChunkPacket(Chunk chunk) {
        this();
        mode = 0;
        this.chunk = chunk;
    }

    @Override
    public void encode(PacketData packetData) {
        packetData.writeByte(mode);
       // System.out.println(chunk.x + ":" + chunk.z);
        packetData.writeInt(chunk.x);
        packetData.writeInt(chunk.z);

        if(mode == 0) {
            for (int x = 0; x < 16; x++) {
                    for (int z = 0; z < 16; z++) {
                        for (int y = 0; y < Settings.chunkHeight * 16; y++) {
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
                                blockData.add(blockState.readData());
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
            ArrayList<Tuple<BlockState,Integer>> blockList = chunk.getBlockChainedList();
            packetData.writeInt(blockList.size());
            for(Tuple<BlockState,Integer> block : blockList) {
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
            int chunkX = packetData.readInt();
            int chunkZ = packetData.readInt();
            chunk = new Chunk(chunkX, chunkZ, ClientMain.getClient().clientWorld);
            IChunk chunk1 = new ClassicChunk(ClientMain.getClient().newClientWorld,256,chunkX,chunkZ);
            if (mode == 0) {
                for (int x = 0; x < 16; x++) {
                        for (int z = 0; z < 16; z++) {
                            for (int y = 0; y < Settings.chunkHeight * 16; y++) {
                            Block block = Blocks.getBlockWithID(packetData.readShort());
                            chunk1.setBlockState(x,y,z,block.getDefaultState1());
                           // chunk.setBlockState(x, y, z, block.getDefaultState());
                        }
                    }
                }
                ClientMain.getClient().newClientWorld.setChunk((long) chunkX * 16, 0, (long) chunkZ * 16, chunk1);
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
                ArrayList<Tuple<BlockState, Integer>> blockList = new ArrayList<>();
                int length = packetData.readInt();
                for(int x = 0; x < length; x++) {
                    short blockID = packetData.readShort();
                    Block block = Blocks.getBlockWithID(blockID);
                    if(block.hasBlockState()) {
                        blockList.add(new Tuple<>(block.getStateWithData(packetData.readShort()),packetData.readInt()));
                    } else {
                        blockList.add(new Tuple<>(block.getDefaultState(),packetData.readInt()));
                    }
                }
                //old impl
                chunk.setFromChainedList(blockList);
                chunk1 = new ClassicChunk(ClientMain.getClient().newClientWorld,256,chunkX,chunkZ);
                int offset = 0;
                for(Tuple<BlockState, Integer> tuple : blockList) {
                    for(int i = 0; i < tuple.getTypeB(); i++) {
                        int x = offset & 15;
                        int y = offset >> 4 & 255;
                        int z = offset >> 12 & 15;
                        chunk1.setBlockState(x,y,z,tuple.getTypeA().getBlock().getDefaultState1());
                        offset++;
                    }
                }
                ClientMain.getClient().newClientWorld.setChunk((long) chunkX * 16, 0, (long) chunkZ * 16, chunk1);
            }
           // WorldCompressor.asCompressedStream(chunk);
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
    }

    @Override
    public void handle() {
        try {
            chunk.world.setChunk(chunk);
            //System.out.println(chunk.x + ":" + chunk.z);
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
    }
}
