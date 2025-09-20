package dev.hilligans.ourcraft.network.packet;

import dev.hilligans.engine.network.engine.ClientNetworkEntity;
import dev.hilligans.engine.network.engine.NetworkEntity;
import dev.hilligans.ourcraft.client.Client;
import dev.hilligans.ourcraft.network.ServerToClientPacketType;
import dev.hilligans.engine.util.IByteArray;

public class SServerExceptionPacket extends ServerToClientPacketType {

    public static final SServerExceptionPacket instance = new SServerExceptionPacket();

    public static void send(NetworkEntity entity, Exception cause) {
        entity.sendPacket(instance.encode(entity, cause));
    }

    public IByteArray encode(NetworkEntity entity, Exception cause) {
        IByteArray array = getWriteArray(entity);
        array.writeUTF16(cause.getMessage());
        return array;
    }

    @Override
    public void decode(ClientNetworkEntity<Client> entity, IByteArray data) {
        String message = data.readUTF16();
        System.err.println("Server exception:\n" + message);
    }
}
