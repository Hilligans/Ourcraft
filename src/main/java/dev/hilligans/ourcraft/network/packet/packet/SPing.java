package dev.hilligans.ourcraft.network.packet.packet;

import dev.hilligans.ourcraft.network.engine.ClientNetworkEntity;
import dev.hilligans.ourcraft.network.engine.NetworkEntity;
import dev.hilligans.ourcraft.network.packet.PacketType;
import dev.hilligans.ourcraft.network.packet.ServerToClientPacketType;
import dev.hilligans.ourcraft.util.IByteArray;

public class SPing extends ServerToClientPacketType {

    public static final SPing instance = new SPing();

    public static void send(NetworkEntity networkEntity) {
        networkEntity.sendPacket(instance.encode(networkEntity));
    }

    public IByteArray encode(NetworkEntity entity) {
        return getWriteArray(entity);
    }

    @Override
    public void decode(ClientNetworkEntity entity, IByteArray data) {

    }
}
