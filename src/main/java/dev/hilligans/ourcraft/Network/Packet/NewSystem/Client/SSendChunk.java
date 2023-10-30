package dev.hilligans.ourcraft.Network.Packet.NewSystem.Client;

import dev.hilligans.ourcraft.ClientMain;
import dev.hilligans.ourcraft.Network.IClientPacketHandler;
import dev.hilligans.ourcraft.Network.PacketBase;
import dev.hilligans.ourcraft.Network.PacketBaseNew;
import dev.hilligans.ourcraft.Network.PacketData;
import dev.hilligans.ourcraft.Ourcraft;
import dev.hilligans.ourcraft.World.Chunk;
import dev.hilligans.ourcraft.World.NewWorldSystem.CubicChunk;
import dev.hilligans.ourcraft.World.NewWorldSystem.IChunk;

public class SSendChunk extends PacketBaseNew<IClientPacketHandler> {


    public byte mode;
    public IChunk newChunk;

    public SSendChunk() {
        mode = 0;
    }

    public SSendChunk(IChunk chunk) {
        mode = 0;
        this.newChunk = chunk;
    }

    @Override
    public void encode(PacketData packetData) {
        try {
            int pos = Ourcraft.chainedChunkStream.fillBuffer(packetData.byteBuf, packetData.size, newChunk);
            packetData.size = pos;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void decode(PacketData packetData) {
        newChunk = new CubicChunk(ClientMain.getClient().newClientWorld,32,0,0, 0);
        try {
            Ourcraft.chainedChunkStream.fillChunk(packetData.byteBuf, packetData.size, newChunk);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handle(IClientPacketHandler iClientPacketHandler) {
        iClientPacketHandler.getWorld().setChunk(newChunk.getBlockX(), newChunk.getBlockY(),  newChunk.getBlockZ(), newChunk);
    }
}
