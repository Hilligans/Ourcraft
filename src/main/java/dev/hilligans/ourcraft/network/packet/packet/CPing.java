package dev.hilligans.ourcraft.network.packet.packet;

import dev.hilligans.ourcraft.network.engine.NetworkEntity;
import dev.hilligans.ourcraft.network.engine.ServerNetworkEntity;
import dev.hilligans.ourcraft.network.packet.ClientToServerPacketType;
import dev.hilligans.ourcraft.util.IByteArray;

public class CPing extends ClientToServerPacketType {

    public static final CPing instance = new CPing();

    public static void send(NetworkEntity networkEntity, int num) {
        networkEntity.sendPacket(instance.encode(networkEntity, num));
    }

    public static IByteArray get(NetworkEntity entity, int num) {
        return instance.encode(entity, num);
    }

    public IByteArray encode(NetworkEntity entity, int num) {
        IByteArray array = getWriteArray(entity);
        array.writeInt(num);
        return array;
    }

    @Override
    public void decode(ServerNetworkEntity entity, IByteArray data) {
        SPing.send(entity, data.readInt());
    }
}
