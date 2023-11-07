package dev.hilligans.ourcraft.network.packet.server;

import dev.hilligans.ourcraft.mod.handler.content.ModContent;
import dev.hilligans.ourcraft.network.IPacketByteArray;
import dev.hilligans.ourcraft.network.PacketBase;
import dev.hilligans.ourcraft.Ourcraft;

public class SSendModContentPacket extends PacketBase {

    ModContent modContent;

    public SSendModContentPacket() {
        super(26);
    }

    public SSendModContentPacket(ModContent modContent) {
        this();
        this.modContent = modContent;
    }

    @Override
    public void encode(IPacketByteArray packetData) {
        modContent.putData(packetData);
    }

    @Override
    public void decode(IPacketByteArray packetData) {
        modContent = new ModContent(packetData, Ourcraft.GAME_INSTANCE);
    }

    @Override
    public void handle() {
        Ourcraft.GAME_INSTANCE.CONTENT_PACK.putMod(modContent);
        Ourcraft.GAME_INSTANCE.CONTENT_PACK.generateData();
    }
}
