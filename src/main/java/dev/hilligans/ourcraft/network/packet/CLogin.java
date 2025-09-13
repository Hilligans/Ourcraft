package dev.hilligans.ourcraft.network.packet;

import dev.hilligans.engine.GameInstance;
import dev.hilligans.ourcraft.data.other.server.ServerPlayerData;
import dev.hilligans.ourcraft.entity.living.entities.PlayerEntity;
import dev.hilligans.engine.network.Protocol;
import dev.hilligans.engine.network.engine.NetworkEntity;
import dev.hilligans.engine.network.engine.ServerNetworkEntity;
import dev.hilligans.engine.network.packet.ClientToServerPacketType;
import dev.hilligans.engine.authentication.IAccount;
import dev.hilligans.engine.util.IByteArray;

public class CLogin extends ClientToServerPacketType {

    public static final CLogin instance = new CLogin();

    public static void send(NetworkEntity networkEntity, String username, String authScheme) {
        networkEntity.sendPacket(instance.encode(networkEntity, username, authScheme));
    }

    public static IByteArray get(NetworkEntity networkEntity, String username, String authScheme)  {
        return instance.encode(networkEntity, username, authScheme);
    }

    public IByteArray encode(NetworkEntity entity, String username, String authScheme) {
        IByteArray array = getWriteArray(entity);
        array.writeString(username);
        array.writeString(authScheme);
        return array;
    }

    public void decode(ServerNetworkEntity entity, IByteArray data) {
        final GameInstance gameInstance = entity.getGameInstance();

        String username = data.readString();
        String authScheme = data.readString();

        IAccount<?> account = entity.getServer().authenticate(authScheme, username, data);

        Protocol newProtocol = entity.getGameInstance().getExcept("ourcraft:Play", Protocol.class);
        SSwitchProtocol.send(entity, newProtocol);
        entity.switchProtocol(newProtocol);


        ServerPlayerData serverPlayerData = entity.getServer().loadPlayer(username, entity);
        serverPlayerData.getServer().sendPacket(entity.getSendProtocol(), SSendMessage.get(entity, username + " has joined."));
        serverPlayerData.networkEntity = entity;
        PlayerEntity playerEntity = serverPlayerData.playerEntity;
        entity.setServerPlayerData(serverPlayerData);
        serverPlayerData.getWorld().sendChunksToPlayer((int) playerEntity.getX(), (int) playerEntity.getY(), (int) playerEntity.getZ(), serverPlayerData);
    }
}
