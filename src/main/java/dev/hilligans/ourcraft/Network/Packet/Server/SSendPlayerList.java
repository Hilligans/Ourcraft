package dev.hilligans.ourcraft.Network.Packet.Server;

import dev.hilligans.ourcraft.ClientMain;
import dev.hilligans.ourcraft.Data.Other.PlayerList;
import dev.hilligans.ourcraft.Network.PacketBase;
import dev.hilligans.ourcraft.Network.PacketData;

public class SSendPlayerList extends PacketBase {

    byte mode;
    String[] players;
    int[] playerIds;

    public SSendPlayerList() {
        super(24);
    }

    public SSendPlayerList(String player, int id, boolean join) {
        this();
        mode = (byte)(join ? 1 : 2);
        players = new String[]{ player };
        playerIds = new int[]{ id };
    }

    public SSendPlayerList(String[] players, int[] playerIds) {
        this();
        this.players = players;
        this.playerIds = playerIds;
    }

    @Override
    public void encode(PacketData packetData) {
        packetData.writeByte(mode);
        packetData.writeShort((short) players.length);
        for(int x = 0; x < players.length; x++) {
            packetData.writeString(players[x]);
            packetData.writeInt(playerIds[x]);
        }
    }

    @Override
    public void decode(PacketData packetData) {
        mode = packetData.readByte();
        short length = packetData.readShort();
        players = new String[length];
        playerIds = new int[length];
        for(int x = 0; x < length; x++) {
            players[x] = packetData.readString();
            playerIds[x] = packetData.readInt();
        }
    }

    @Override
    public void handle() {
        if(mode == 0) {
            ClientMain.getClient().playerList = new PlayerList(players,playerIds);
        } else if(mode == 1) {
            ClientMain.getClient().playerList.addPlayers(players,playerIds);
        } else {
            ClientMain.getClient().playerList.removePlayers(players,playerIds);
        }
    }
}
