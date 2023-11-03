package dev.hilligans.ourcraft.Network.Packet.NewSystem.Server;

import dev.hilligans.ourcraft.Network.*;

public class SSendWorldSystemFormat extends PacketBaseNew<IClientPacketHandler> {

    String worldName;
    int chunkWidth;
    int chunkHeight;

    @Override
    public void encode(IPacketByteArray packetData) {
        packetData.writeUTF8(worldName);
        packetData.writeVarInt(chunkWidth);
        packetData.writeVarInt(chunkHeight);
    }

    @Override
    public void decode(IPacketByteArray packetData) {
        this.worldName = packetData.readUTF8();
        this.chunkWidth = packetData.readVarInt();
        this.chunkHeight = packetData.readVarInt();
    }

    @Override
    public void handle(IClientPacketHandler iClientPacketHandler) {

    }
}
