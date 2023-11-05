package dev.hilligans.ourcraft.Network.Packet.Client;

import dev.hilligans.ourcraft.Data.Other.Server.ServerPlayerData;
import dev.hilligans.ourcraft.Entity.Entity;
import dev.hilligans.ourcraft.Entity.LivingEntities.PlayerEntity;
import dev.hilligans.ourcraft.ModHandler.Content.ModContent;
import dev.hilligans.ourcraft.Network.*;
import dev.hilligans.ourcraft.Ourcraft;
import dev.hilligans.ourcraft.ServerMain;
import dev.hilligans.ourcraft.Network.Packet.Server.*;

import java.util.ArrayList;

public class CRequestContent extends PacketBaseNew<IServerPacketHandler> {

    ArrayList<String> mods;

    public CRequestContent() {
        super(27);
    }

    public CRequestContent(ArrayList<String> mods) {
        this();
        this.mods = mods;
    }

    @Override
    public void encode(IPacketByteArray packetData) {
        packetData.writeInt(mods.size());
        for(String string : mods) {
            packetData.writeUTF16(string);
        }
    }

    @Override
    public void decode(IPacketByteArray packetData) {
        int length = packetData.readInt();
        mods = new ArrayList<>();
        for(int x = 0; x < length; x++) {
            mods.add(packetData.readUTF16());
        }
    }

    @Override
    public void handle(IServerPacketHandler serverPacketHandler) {
        for(String string : mods) {
            ModContent modContent = Ourcraft.GAME_INSTANCE.CONTENT_PACK.mods.get(string.split(":::")[0]);
            serverPacketHandler.sendPacket(new SSendModContentPacket(modContent), ctx);
        }

        ServerPlayerData serverPlayerData = serverPacketHandler.getServerPlayerData();
        PlayerEntity playerEntity = serverPlayerData.playerEntity;

        int size = serverPacketHandler.getServerNetworkHandler().mappedPlayerData.size();
        String[] players = new String[size];
        int[] ids = new int[size];
        int x = 0;
        for(ServerPlayerData playerData : serverPacketHandler.getServerNetworkHandler().mappedPlayerData.values()) {
            players[x] = playerData.getPlayerName();
            ids[x] = (int) playerData.getPlayerID().l1;
            x++;
        }
        serverPacketHandler.getServer().sendPacket(new SSendPlayerList(players,ids));
        for(Entity entity : ServerMain.getWorld(serverPlayerData.getDimension()).entities.values()) {
            serverPacketHandler.sendPacket(new SCreateEntityPacket(entity),ctx);
        }
        serverPacketHandler.sendPacket(new SUpdatePlayer((float) playerEntity.getX(), (float) playerEntity.getY(), (float) playerEntity.getZ(),0,0),ctx);
        serverPlayerData.playerInventory.age++;

    //    if(version != ServerSidedData.getInstance().version) {
       //     ServerSidedData.getInstance().sendDataToClient(ctx);
      //  }

        serverPacketHandler.sendPacket(new SUpdateInventory(serverPlayerData.playerInventory),ctx);


    }
}
