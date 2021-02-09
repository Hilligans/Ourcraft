package Hilligans.Network.Packet.Client;

import Hilligans.Network.Packet.Server.SSendChunkPacket;
import Hilligans.Network.PacketBase;
import Hilligans.World.Chunk;
import Hilligans.Network.PacketData;
import Hilligans.Network.ServerNetworkHandler;
import Hilligans.ServerMain;

public class CRequestChunkPacket extends PacketBase {

    public int ChunkX;
    public int ChunkZ;

    public CRequestChunkPacket(int ChunkX, int ChunkZ) {
        super(1);
        this.ChunkX = ChunkX;
        this.ChunkZ = ChunkZ;
    }

    public CRequestChunkPacket() {
        this(0,0);
    }

    @Override
    public void encode(PacketData packetData) {
        packetData.writeInt(ChunkX);
        packetData.writeInt(ChunkZ);
    }

    @Override
    public void decode(PacketData packetData) {
        ChunkX = packetData.readInt();
        ChunkZ = packetData.readInt();
    }

    @Override
    public void handle() {
        Chunk chunk = ServerMain.world.getOrGenerateChunk(ChunkX, ChunkZ);
        ServerMain.world.generateChunk(ChunkX + 1, ChunkZ);
        ServerMain.world.generateChunk(ChunkX - 1, ChunkZ);
        ServerMain.world.generateChunk(ChunkX, ChunkZ + 1);
        ServerMain.world.generateChunk(ChunkX, ChunkZ - 1);

        if(chunk != null) {
            ServerNetworkHandler.sendPacket(new SSendChunkPacket(chunk),ctx);
        }
    }

}
