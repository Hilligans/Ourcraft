package dev.hilligans.ourcraft.network.packet.packet;

import dev.hilligans.ourcraft.network.Protocol;
import dev.hilligans.ourcraft.network.engine.ClientNetworkEntity;
import dev.hilligans.ourcraft.network.engine.NetworkEntity;
import dev.hilligans.ourcraft.network.packet.ServerToClientPacketType;
import dev.hilligans.ourcraft.util.IByteArray;

public class SSwitchProtocol extends ServerToClientPacketType {

    public static final SSwitchProtocol instance = new SSwitchProtocol();

    public static void send(NetworkEntity entity, Protocol next) {
        entity.sendPacket(instance.encode(entity, next));
    }

    public static IByteArray get(NetworkEntity entity, Protocol next) {
        return instance.encode(entity, next);
    }

    public IByteArray encode(NetworkEntity entity, Protocol next) {
        IByteArray array = getWriteArray(entity);
        array.writeUTF16(next.getIdentifierName());
        return array;
    }

    @Override
    public void decode(ClientNetworkEntity entity, IByteArray data) {
        String protocolName = data.readUTF16();
        Protocol protocol = entity.getGameInstance().getExcept(protocolName, Protocol.class);
        entity.switchProtocol(protocol);
    }
}
