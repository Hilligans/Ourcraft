package dev.hilligans.ourcraft.Network.Packet.NewSystem.Server;

import dev.hilligans.ourcraft.Network.*;
import dev.hilligans.ourcraft.Ourcraft;
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
    public void encode(IPacketByteArray packetData) {
        try {
            int pos = Ourcraft.chainedChunkStream.fillBuffer(packetData.getByteBuf(), (int)packetData.readerIndex(), newChunk);
            packetData.setReaderIndex(pos);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void decode(IPacketByteArray packetData) {
        newChunk = new CubicChunk(32,0,0, 0);
        try {
            Ourcraft.chainedChunkStream.fillChunk(packetData.getByteBuf(), (int)packetData.readerIndex(), newChunk);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handle(IClientPacketHandler iClientPacketHandler) {
        iClientPacketHandler.getWorld().setChunk(newChunk.getBlockX(), newChunk.getBlockY(),  newChunk.getBlockZ(), newChunk.setWorld(iClientPacketHandler.getWorld()));
    }
}
