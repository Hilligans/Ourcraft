package dev.hilligans.ourcraft.network.packet.packet;

import dev.hilligans.ourcraft.data.other.server.ServerPlayerData;
import dev.hilligans.ourcraft.entity.living.entities.PlayerEntity;
import dev.hilligans.ourcraft.network.Protocol;
import dev.hilligans.ourcraft.network.engine.NetworkEntity;
import dev.hilligans.ourcraft.network.engine.ServerNetworkEntity;
import dev.hilligans.ourcraft.network.packet.ClientToServerPacketType;
import dev.hilligans.ourcraft.network.packet.PacketType;
import dev.hilligans.ourcraft.util.IByteArray;

public class CLogin extends ClientToServerPacketType {

    public static final CLogin instance = new CLogin();

    public static void send(NetworkEntity networkEntity, String username) {
        networkEntity.sendPacket(instance.encode(networkEntity, username));
    }

    public static IByteArray get(NetworkEntity networkEntity, String username)  {
        return instance.encode(networkEntity, username);
    }

    public IByteArray encode(NetworkEntity entity, String username) {
        IByteArray array = getWriteArray(entity);
        array.writeString(username);
        return array;
    }

    public void decode(ServerNetworkEntity entity, IByteArray data) {
        String username = data.readString();

        Protocol newProtocol = entity.getGameInstance().getExcept("ourcraft:Play", Protocol.class);
        SSwitchProtocol.send(entity, newProtocol);
        entity.switchProtocol(newProtocol);

        ServerPlayerData serverPlayerData = entity.getServer().loadPlayer(username, entity);
        serverPlayerData.getServer().sendPacket(entity.getSendProtocol(), SSendMessage.get(entity, username + " has joined."));
        serverPlayerData.networkEntity = entity;
        PlayerEntity playerEntity = serverPlayerData.playerEntity;
        serverPlayerData.getWorld().sendChunksToPlayer((int) playerEntity.getX(), (int) playerEntity.getY(), (int) playerEntity.getZ(), serverPlayerData);
    }
}
