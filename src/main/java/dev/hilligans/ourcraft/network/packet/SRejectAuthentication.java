package dev.hilligans.ourcraft.network.packet;

import dev.hilligans.engine.network.engine.ClientNetworkEntity;
import dev.hilligans.engine.network.engine.NetworkEntity;
import dev.hilligans.ourcraft.client.Client;
import dev.hilligans.ourcraft.network.ServerToClientPacketType;
import dev.hilligans.engine.util.IByteArray;

public class SRejectAuthentication extends ServerToClientPacketType {

    public static final SRejectAuthentication instance = new SRejectAuthentication();

    public static void send(NetworkEntity entity, String message) {
        entity.sendPacket(instance.encode(entity, message));
    }

    public IByteArray encode(NetworkEntity entity, String message) {
        IByteArray array = getWriteArray(entity);
        array.writeString(message);
        return array;
    }

    @Override
    public void decode(ClientNetworkEntity<Client> entity, IByteArray data) {
        String message = data.readString();
        System.err.println("SRejectAuthentication: " + message);
    }
}
