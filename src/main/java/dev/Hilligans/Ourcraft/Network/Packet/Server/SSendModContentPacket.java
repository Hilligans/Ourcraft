package dev.Hilligans.Ourcraft.Network.Packet.Server;

import dev.Hilligans.Ourcraft.ModHandler.Content.ModContent;
import dev.Hilligans.Ourcraft.Network.PacketBase;
import dev.Hilligans.Ourcraft.Network.PacketData;
import dev.Hilligans.Ourcraft.Ourcraft;

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
