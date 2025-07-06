package dev.hilligans.ourcraft.network.packet.packet;

import dev.hilligans.ourcraft.client.Client;
import dev.hilligans.ourcraft.network.engine.ClientNetworkEntity;
import dev.hilligans.ourcraft.network.engine.NetworkEntity;
import dev.hilligans.ourcraft.network.packet.ServerToClientPacketType;
import dev.hilligans.ourcraft.util.IByteArray;
import dev.hilligans.ourcraft.world.newworldsystem.IChunk;

public class SChatMessage extends ServerToClientPacketType {

    public static final SChatMessage instance = new SChatMessage();

    public static void send(NetworkEntity entity, String message) {
        entity.sendPacket(instance.encode(entity, message));
    }

    public static IByteArray get(NetworkEntity networkEntity, String message) {
        return instance.encode(networkEntity, message);
    }

    public IByteArray encode(NetworkEntity entity, String message) {
        IByteArray array = getWriteArray(entity);
        array.writeUTF16(message);
        return array;
    }

    @Override
    public void decode(ClientNetworkEntity entity, IByteArray data) {
        entity.getClient().chatMessages.addMessage(data.readUTF16());
    }
}
