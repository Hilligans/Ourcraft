package dev.Hilligans.Ourcraft.Network.Packet.Client;

import dev.Hilligans.Ourcraft.Network.Packet.Server.SSendChunkPacket;
import dev.Hilligans.Ourcraft.Network.PacketBase;
import dev.Hilligans.Ourcraft.ServerMain;
import dev.Hilligans.Ourcraft.World.Chunk;
import dev.Hilligans.Ourcraft.Network.PacketData;
import dev.Hilligans.Ourcraft.Network.ServerNetworkHandler;

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
        try {
            int dim = ServerNetworkHandler.getPlayerData(ctx).getDimension();
            Chunk chunk = ServerMain.getWorld(dim).getOrGenerateChunk(ChunkX, ChunkZ);
            ServerMain.getWorld(dim).generateChunk(ChunkX + 1, ChunkZ);
            ServerMain.getWorld(dim).generateChunk(ChunkX - 1, ChunkZ);
            ServerMain.getWorld(dim).generateChunk(ChunkX, ChunkZ + 1);
            ServerMain.getWorld(dim).generateChunk(ChunkX, ChunkZ - 1);

            if (chunk != null) {
                ServerNetworkHandler.sendPacket(new SSendChunkPacket(chunk), ctx);
            }
        } catch (Exception ignored) {}
    }

}
