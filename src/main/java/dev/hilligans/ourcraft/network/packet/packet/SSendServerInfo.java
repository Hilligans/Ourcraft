package dev.hilligans.ourcraft.network.packet.packet;

import dev.hilligans.ourcraft.network.engine.ClientNetworkEntity;
import dev.hilligans.ourcraft.network.engine.NetworkEntity;
import dev.hilligans.ourcraft.network.packet.ServerToClientPacketType;
import dev.hilligans.ourcraft.server.IServer;
import dev.hilligans.ourcraft.util.IByteArray;

public class SSendServerInfo extends ServerToClientPacketType {

    public static SSendServerInfo instance = new SSendServerInfo();

    public static void send(NetworkEntity entity, IServer server) {
        entity.sendPacket(instance.encode(entity, server));
    }

    public IByteArray encode(NetworkEntity entity, IServer server) {
        IByteArray array = getWriteArray(entity);

        return array;
    }

    @Override
    public void decode(ClientNetworkEntity entity, IByteArray data) {

    }
}
