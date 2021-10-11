package Hilligans.Network.Packet.Client;

import Hilligans.Data.Other.Server.ServerPlayerData;
import Hilligans.Entity.Entity;
import Hilligans.Entity.LivingEntities.PlayerEntity;
import Hilligans.ModHandler.Content.ModContent;
import Hilligans.Network.Packet.Server.*;
import Hilligans.Network.PacketBase;
import Hilligans.Network.PacketData;
import Hilligans.Network.ServerNetworkHandler;
import Hilligans.Ourcraft;
import Hilligans.ServerMain;
import io.netty.channel.ChannelId;

import java.util.ArrayList;

public class CRequestContent extends PacketBase {

    ArrayList<String> mods;

    public CRequestContent() {
        super(27);
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
            ModContent modContent = Ourcraft.GAME_INSTANCE.CONTENT_PACK.mods.get(string.split(":::")[0]);
            ServerNetworkHandler.sendPacket(new SSendModContentPacket(modContent), ctx);
        }

        ServerPlayerData serverPlayerData = ServerNetworkHandler.getPlayerData(ctx);
        PlayerEntity playerEntity = serverPlayerData.playerEntity;

        int size = ServerNetworkHandler.mappedChannels.size();
        String[] players = new String[size];
        int[] ids = new int[size];
        int x = 0;
        for(ChannelId channelId1 : ServerNetworkHandler.mappedChannels.values()) {
            players[x] = ServerNetworkHandler.mappedName.get(channelId1);
            ids[x] = ServerNetworkHandler.mappedId.get(channelId1);
            x++;
        }
        ServerMain.getServer().sendPacket(new SSendPlayerList(players,ids));
        for(Entity entity : ServerMain.getWorld(serverPlayerData.getDimension()).entities.values()) {
            ServerNetworkHandler.sendPacket(new SCreateEntityPacket(entity),ctx);
        }
        ServerNetworkHandler.sendPacket(new SUpdatePlayer(playerEntity.x,playerEntity.y,playerEntity.z,0,0),ctx);
        serverPlayerData.playerInventory.age++;

    //    if(version != ServerSidedData.getInstance().version) {
       //     ServerSidedData.getInstance().sendDataToClient(ctx);
      //  }

        ServerNetworkHandler.sendPacket(new SUpdateInventory(serverPlayerData.playerInventory),ctx);


    }
}
