package dev.hilligans.ourcraft.network.packet;

import dev.hilligans.engine.network.engine.NetworkEntity;
import dev.hilligans.engine.network.engine.ServerNetworkEntity;
import dev.hilligans.engine.network.packet.ClientToServerPacketType;
import dev.hilligans.engine.util.IByteArray;

public class CGetServerInfo extends ClientToServerPacketType {

    public static final CGetServerInfo instance = new CGetServerInfo();

    public static void send(NetworkEntity entity) {
        entity.sendPacket(instance.encode(entity));
    }

    public static IByteArray get(NetworkEntity entity) {
        return instance.encode(entity);
    }

    public IByteArray encode(NetworkEntity entity) {
        return getWriteArray(entity);
    }

    @Override
    public void decode(ServerNetworkEntity entity, IByteArray data) {
        SSendServerInfo.send(entity, entity.getServer());
    }
}
