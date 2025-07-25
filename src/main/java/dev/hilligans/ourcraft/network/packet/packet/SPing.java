package dev.hilligans.ourcraft.network.packet.packet;

import dev.hilligans.ourcraft.network.engine.ClientNetworkEntity;
import dev.hilligans.ourcraft.network.engine.NetworkEntity;
import dev.hilligans.ourcraft.network.packet.ServerToClientPacketType;
import dev.hilligans.ourcraft.util.IByteArray;

public class SPing extends ServerToClientPacketType {

    public static final SPing instance = new SPing();

    public static void send(NetworkEntity networkEntity, int num) {
        networkEntity.sendPacket(instance.encode(networkEntity, num));
    }

    public IByteArray encode(NetworkEntity entity, int num) {
        IByteArray array = getWriteArray(entity);
        array.writeInt(num);
        return array;
    }

    @Override
    public void decode(ClientNetworkEntity entity, IByteArray data) {

    }
}
