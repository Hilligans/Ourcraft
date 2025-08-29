package dev.hilligans.ourcraft.network.packet.packet;

import dev.hilligans.ourcraft.network.engine.ClientNetworkEntity;
import dev.hilligans.ourcraft.network.engine.NetworkEntity;
import dev.hilligans.ourcraft.network.packet.ServerToClientPacketType;
import dev.hilligans.ourcraft.util.IByteArray;

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
    public void decode(ClientNetworkEntity entity, IByteArray data) {
        String message = data.readString();
        System.err.println("SRejectAuthentication: " + message);
    }
}
