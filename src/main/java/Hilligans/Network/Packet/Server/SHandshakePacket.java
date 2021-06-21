package Hilligans.Network.Packet.Server;

import Hilligans.ClientMain;
import Hilligans.Data.Other.ServerSidedData;
import Hilligans.ModHandler.Content.ContentPack;
import Hilligans.Network.ClientNetworkHandler;
import Hilligans.Network.Packet.Client.CRequestContent;
import Hilligans.Network.PacketBase;
import Hilligans.Network.PacketData;
import Hilligans.Network.ServerNetworkHandler;
import Hilligans.Ourcraft;

import java.util.ArrayList;
import java.util.Arrays;

public class SHandshakePacket extends PacketBase {

    public int playerId;
    public long dataVersion;
    public String[] mods;

    public SHandshakePacket() {
        super(6);
    }

    public SHandshakePacket(int playerId, long dataVersion) {
        this();
        this.playerId = playerId;
        this.dataVersion = dataVersion;
    }

    @Override
    public void encode(PacketData packetData) {
        packetData.writeInt(playerId);
        packetData.writeLong(dataVersion);
        String[] mods = Ourcraft.CONTENT_PACK.getModList();
        packetData.writeInt(mods.length);
        for(String string : mods) {
            packetData.writeString(string);
        }
    }

    @Override
    public void decode(PacketData packetData) {
        playerId = packetData.readInt();
        dataVersion = packetData.readLong();
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
        ServerSidedData serverSidedData = ServerSidedData.getInstance();
        if(serverSidedData.version != dataVersion) {
            serverSidedData.clear();
            serverSidedData.version = dataVersion;
        }

        ArrayList<String> localMods = new ArrayList<>(Arrays.asList(Ourcraft.CONTENT_PACK.getModList()));
        ArrayList<String> neededMods = new ArrayList<>();
        for(String string : mods) {
            if(!localMods.contains(string)) {
                neededMods.add(string);
            }
        }
        if(neededMods.size() != 0) {
            ClientNetworkHandler.sendPacketDirect(new CRequestContent(neededMods));
        }
    }
}
