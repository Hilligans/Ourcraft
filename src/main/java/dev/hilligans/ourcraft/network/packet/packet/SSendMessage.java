package dev.hilligans.ourcraft.network.packet.packet;

import dev.hilligans.ourcraft.network.engine.ClientNetworkEntity;
import dev.hilligans.ourcraft.network.engine.NetworkEntity;
import dev.hilligans.ourcraft.network.packet.ServerToClientPacketType;
import dev.hilligans.ourcraft.util.IByteArray;

public class SSendMessage extends ServerToClientPacketType {

    public static final SSendMessage instance = new SSendMessage();

    public static void send(NetworkEntity entity, String message) {
        entity.sendPacket(instance.encode(entity, message));
    }

    public static IByteArray get(NetworkEntity entity, String message) {
        return instance.encode(entity, message);
    }

    public IByteArray encode(NetworkEntity entity, String message) {
        IByteArray array = getWriteArray(entity);
        array.writeUTF16(message);
        return array;
    }

    @Override
    public void decode(ClientNetworkEntity entity, IByteArray data) {
        String message = data.readUTF16();
        entity.getClient().chatMessages.addMessage(message);
    }
}
