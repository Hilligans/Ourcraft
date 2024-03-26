package dev.hilligans.ourcraft.network.packet.server;

import dev.hilligans.ourcraft.GameInstance;
import dev.hilligans.ourcraft.client.Client;
import dev.hilligans.ourcraft.network.*;
import dev.hilligans.ourcraft.Ourcraft;
import dev.hilligans.ourcraft.util.Settings;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class SHandshakePacket extends PacketBaseNew<IClientPacketHandler> {

    public int playerId;
    public String[] mods;
    public GameInstance gameInstance;

    public SHandshakePacket() {
        super(6);
    }

    public SHandshakePacket(int playerId, GameInstance gameInstance) {
        this();
        this.gameInstance = gameInstance;
        this.playerId = playerId;
    }

    @Override
    public void encode(IPacketByteArray packetData) {
        packetData.writeInt(playerId);
        String[] mods = gameInstance.CONTENT_PACK.getModList();
        packetData.writeInt(mods.length);
        for(String string : mods) {
            packetData.writeString(string);
        }
    }

    @Override
    public void decode(IPacketByteArray packetData) {
        playerId = packetData.readInt();
        int length = packetData.readInt();
        mods = new String[length];
        for(int x = 0; x < length; x++) {
            mods[x] = packetData.readString();
        }
    }

    @Override
    public void handle(IClientPacketHandler clientPacketHandler) {
        Client client = clientPacketHandler.getClient();
        gameInstance = client.getGameInstance();
        client.playerId = playerId;
        client.valid = true;

        ArrayList<String> localMods = new ArrayList<>(Arrays.asList(gameInstance.CONTENT_PACK.getModList()));
        ArrayList<String> neededMods = new ArrayList<>();
        for(String string : mods) {
            if(!localMods.contains(string)) {
                if(!new File("mod_cache/" + (Settings.storeServerModsIndividually ? "servers/" + client.serverIP.replace(':','_') + "/" : "mods/") + string.replace(":::","-") + ".dat").exists()) {
                    neededMods.add(string);
                } else {
                    gameInstance.CONTENT_PACK.loadCachedMod(string.replace(":::","-"), client);
                }
            }
        }
        if(neededMods.size() != 0) {

          //  ctx.channel().writeAndFlush(new PacketData(new CRequestContent(neededMods)));
        } else if(mods.length != 0) {
           // gameInstance.CONTENT_PACK.generateData();
        }
    }
}
