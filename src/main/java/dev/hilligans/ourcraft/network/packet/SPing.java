package dev.hilligans.ourcraft.network.packet;

import dev.hilligans.engine.network.engine.ClientNetworkEntity;
import dev.hilligans.engine.network.engine.NetworkEntity;
import dev.hilligans.engine.network.packet.ServerToClientPacketType;
import dev.hilligans.engine.util.IByteArray;

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
