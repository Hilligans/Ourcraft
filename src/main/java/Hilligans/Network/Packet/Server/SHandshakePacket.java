package Hilligans.Network.Packet.Server;

import Hilligans.ClientMain;
import Hilligans.ModHandler.Content.ContentPack;
import Hilligans.Network.ClientNetworkHandler;
import Hilligans.Network.Packet.Client.CRequestContent;
import Hilligans.Network.PacketBase;
import Hilligans.Network.PacketData;
import Hilligans.Ourcraft;
import Hilligans.Util.Settings;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class SHandshakePacket extends PacketBase {

    public int playerId;
    public String[] mods;

    public SHandshakePacket() {
        super(6);
    }

    public SHandshakePacket(int playerId) {
        this();
        this.playerId = playerId;
    }

    @Override
    public void encode(PacketData packetData) {
        packetData.writeInt(playerId);
        String[] mods = Ourcraft.CONTENT_PACK.getModList();
        packetData.writeInt(mods.length);
        for(String string : mods) {
            packetData.writeString(string);
        }
    }

    @Override
    public void decode(PacketData packetData) {
        playerId = packetData.readInt();
        int length = packetData.readInt();
        mods = new String[length];
        for(int x = 0; x < length; x++) {
            mods[x] = packetData.readString();
        }
    }

    @Override
    public void handle() {
        ClientMain.getClient().playerId = playerId;
        ClientMain.getClient().valid = true;

        ArrayList<String> localMods = new ArrayList<>(Arrays.asList(Ourcraft.CONTENT_PACK.getModList()));
        ArrayList<String> neededMods = new ArrayList<>();
        for(String string : mods) {
            if(!localMods.contains(string)) {
                if(!new File("mod_cache/" + (Settings.storeServerModsIndividually ? "servers/" + ClientMain.getClient().serverIP.replace(':','_') + "/" : "mods/") + string.replace(":::","-") + ".dat").exists()) {
                    neededMods.add(string);
                } else {
                    Ourcraft.CONTENT_PACK.loadCachedMod(string.replace(":::","-"));
                }
            }
        }
        if(neededMods.size() != 0) {
            ClientNetworkHandler.sendPacketDirect(new CRequestContent(neededMods));
        } else if(mods.length != 0) {
            Ourcraft.CONTENT_PACK.generateData();
        }
    }
}
