package dev.Hilligans.Network.Packet.Server;

import dev.Hilligans.ModHandler.Content.ModContent;
import dev.Hilligans.Network.PacketBase;
import dev.Hilligans.Network.PacketData;
import dev.Hilligans.Ourcraft;

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
    public void encode(PacketData packetData) {
        modContent.putData(packetData);
    }

    @Override
    public void decode(PacketData packetData) {
        modContent = new ModContent(packetData, Ourcraft.GAME_INSTANCE);
    }

    @Override
    public void handle() {
        Ourcraft.GAME_INSTANCE.CONTENT_PACK.putMod(modContent);
        Ourcraft.GAME_INSTANCE.CONTENT_PACK.generateData();
    }
}
