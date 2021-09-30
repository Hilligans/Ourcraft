package Hilligans.Network.Packet.Server;

import Hilligans.ModHandler.Content.ModContent;
import Hilligans.Network.PacketBase;
import Hilligans.Network.PacketData;
import Hilligans.Ourcraft;

import java.util.Arrays;

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
        modContent = new ModContent(packetData,Ourcraft.GAME_INSTANCE);
    }

    @Override
    public void handle() {
        Ourcraft.CONTENT_PACK.putMod(modContent);
        Ourcraft.CONTENT_PACK.generateData();
    }
}
