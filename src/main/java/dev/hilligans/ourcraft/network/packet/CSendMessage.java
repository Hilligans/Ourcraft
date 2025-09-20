package dev.hilligans.ourcraft.network.packet;

import dev.hilligans.engine.network.engine.NetworkEntity;
import dev.hilligans.engine.network.engine.ServerNetworkEntity;
import dev.hilligans.ourcraft.network.ClientToServerPacketType;
import dev.hilligans.engine.util.IByteArray;
import dev.hilligans.ourcraft.server.IServer;

public class CSendMessage extends ClientToServerPacketType {

    public static final CSendMessage instance = new CSendMessage();

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
    public void decode(ServerNetworkEntity<IServer> entity, IByteArray data) {
        String message = data.readUTF16();
        entity.getServer().sendPacket(entity.getSendProtocol(), SSendMessage.get(entity, message));
    }
}
