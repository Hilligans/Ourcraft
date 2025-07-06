package dev.hilligans.ourcraft.network.packet.packet;

import dev.hilligans.ourcraft.network.engine.NetworkEntity;
import dev.hilligans.ourcraft.network.engine.ServerNetworkEntity;
import dev.hilligans.ourcraft.network.packet.ClientToServerPacketType;
import dev.hilligans.ourcraft.util.IByteArray;

public class CPing extends ClientToServerPacketType {

    public static final CPing instance = new CPing();

    public static void send(NetworkEntity networkEntity) {
        networkEntity.sendPacket(instance.encode(networkEntity));
    }

    public IByteArray encode(NetworkEntity entity) {
        return getWriteArray(entity);
    }

    @Override
    public void decode(ServerNetworkEntity entity, IByteArray data) {
        SPing.send(entity);
    }
}
