package dev.hilligans.ourcraft.Network.Packet.Client;

import dev.hilligans.ourcraft.Network.*;
import dev.hilligans.ourcraft.Network.Packet.Server.SSendChunkPacket;
import dev.hilligans.ourcraft.World.NewWorldSystem.IChunk;

public class CRequestChunkPacket extends PacketBaseNew<IServerPacketHandler> {

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
    public void encode(IPacketByteArray packetData) {
        packetData.writeInt(ChunkX);
        packetData.writeInt(ChunkZ);

    }

    @Override
    public void decode(IPacketByteArray packetData) {
        ChunkX = packetData.readInt();
        ChunkZ = packetData.readInt();
    }

    @Override
    public void handle(IServerPacketHandler serverPacketHandler) {
        try {

            //System.out.println(ChunkX + ":" + ChunkZ);
            int dim =serverPacketHandler.getServerPlayerData().getDimension();
          /*  Chunk chunk = ServerMain.getWorld(dim).getOrGenerateChunk(ChunkX, ChunkZ);
            ServerMain.getWorld(dim).generateChunk(ChunkX + 1, ChunkZ);
            ServerMain.getWorld(dim).generateChunk(ChunkX - 1, ChunkZ);
            ServerMain.getWorld(dim).generateChunk(ChunkX, ChunkZ + 1);
            ServerMain.getWorld(dim).generateChunk(ChunkX, ChunkZ - 1);
           */
            IChunk chunk = serverPacketHandler.getWorld().getChunkNonNull(ChunkX * 16L,0, ChunkZ * 16L);
            if (chunk != null) {
                serverPacketHandler.sendPacket(new SSendChunkPacket(chunk), ctx);
            }
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
    }
}
