package Hilligans.Network.Packet.Server;

import Hilligans.ClientMain;
import Hilligans.Util.Settings;
import Hilligans.World.Chunk;
import Hilligans.Block.Blocks;
import Hilligans.Network.PacketBase;
import Hilligans.Network.PacketData;
import Hilligans.World.BlockState;

public class SSendChunkPacket extends PacketBase {

    public Chunk chunk;

    public SSendChunkPacket(Chunk chunk) {
        super(2);
        this.chunk = chunk;
    }

    public SSendChunkPacket() {
        this(null);
    }


    @Override
    public void decode(PacketData packetData) {
        chunk = new Chunk(packetData.readInt(),packetData.readInt(), ClientMain.clientWorld);
        for(int x = 0; x < 16; x++) {
            for(int y = 0; y < Settings.chunkHeight * 16; y++) {
                for(int z = 0; z < 16; z++) {
                    chunk.setBlockState(x,y,z,new BlockState(Blocks.getBlockWithID(packetData.readShort())));
                }
            }
        }
    }

    @Override
    public void encode(PacketData packetData) {
        packetData.writeInt(chunk.x);
        packetData.writeInt(chunk.z);
        for(int x = 0; x < 16; x++) {
            for(int y = 0; y < Settings.chunkHeight * 16; y++) {
                for(int z = 0; z < 16; z++) {
                    packetData.writeShort(chunk.getBlockState(x,y,z).block.id);
                }
            }
        }
    }

    @Override
    public void handle() {
        chunk.world.setChunk(chunk);
    }
}
