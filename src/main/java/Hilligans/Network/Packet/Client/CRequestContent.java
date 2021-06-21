package Hilligans.Network.Packet.Client;

import Hilligans.ModHandler.Content.ContentPack;
import Hilligans.ModHandler.Content.ModContent;
import Hilligans.Network.Packet.Server.SSendModContentPacket;
import Hilligans.Network.PacketBase;
import Hilligans.Network.PacketData;
import Hilligans.Network.ServerNetworkHandler;
import Hilligans.Ourcraft;
import Hilligans.ServerMain;

import java.util.ArrayList;

public class CRequestContent extends PacketBase {

    ArrayList<String> mods;

    public CRequestContent() {
        super(31);
    }

    public CRequestContent(ArrayList<String> mods) {
        this();
        this.mods = mods;
    }

    @Override
    public void encode(PacketData packetData) {
        packetData.writeInt(mods.size());
        for(String string : mods) {
            packetData.writeString(string);
        }
    }

    @Override
    public void decode(PacketData packetData) {
        int length = packetData.readInt();
        mods = new ArrayList<>();
        for(int x = 0; x < length; x++) {
            mods.add(packetData.readString());
        }
    }

    @Override
    public void handle() {
        for(String string : mods) {
            ModContent modContent = Ourcraft.CONTENT_PACK.mods.get(string.split(":::")[0]);
            ServerNetworkHandler.sendPacket(new SSendModContentPacket(modContent), ctx);
        }
    }
}
