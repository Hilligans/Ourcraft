package dev.hilligans.ourcraft.network.packet;

import dev.hilligans.engine.network.engine.NetworkEntity;
import dev.hilligans.engine.network.engine.ServerNetworkEntity;
import dev.hilligans.ourcraft.network.ClientToServerPacketType;
import dev.hilligans.engine.util.IByteArray;
import dev.hilligans.ourcraft.server.IServer;

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
    public void decode(ServerNetworkEntity<IServer> entity, IByteArray data) {
        SPing.send(entity, data.readInt());
    }
}
