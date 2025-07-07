package dev.hilligans.ourcraft.network.packet.packet;

import dev.hilligans.ourcraft.network.engine.NetworkEntity;
import dev.hilligans.ourcraft.network.engine.ServerNetworkEntity;
import dev.hilligans.ourcraft.network.packet.ClientToServerPacketType;
import dev.hilligans.ourcraft.util.IByteArray;

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

    }
}
