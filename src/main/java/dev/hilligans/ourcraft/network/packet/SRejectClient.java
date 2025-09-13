package dev.hilligans.ourcraft.network.packet;

import dev.hilligans.engine.network.engine.ClientNetworkEntity;
import dev.hilligans.engine.network.engine.NetworkEntity;
import dev.hilligans.engine.network.packet.ServerToClientPacketType;
import dev.hilligans.engine.util.IByteArray;

public class SRejectClient extends ServerToClientPacketType {

    public static final SRejectClient instance = new SRejectClient();

    public static void send(NetworkEntity entity, String reason) {
        entity.sendPacket(instance.encode(entity, reason));
    }

    public static IByteArray get(NetworkEntity entity, String reason) {
        return instance.encode(entity, reason);
    }

    public IByteArray encode(NetworkEntity entity, String reason) {
        IByteArray array = getWriteArray(entity);
        array.writeUTF16(reason);
        return array;
    }

    @Override
    public void decode(ClientNetworkEntity entity, IByteArray data) {

    }
}
