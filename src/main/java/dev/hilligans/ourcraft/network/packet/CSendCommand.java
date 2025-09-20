package dev.hilligans.ourcraft.network.packet;

import dev.hilligans.engine.command.CommandTree;
import dev.hilligans.ourcraft.command.PlayerExecutor;
import dev.hilligans.engine.network.engine.NetworkEntity;
import dev.hilligans.engine.network.engine.ServerNetworkEntity;
import dev.hilligans.ourcraft.network.ClientToServerPacketType;
import dev.hilligans.engine.util.IByteArray;
import dev.hilligans.ourcraft.server.IServer;

public class CSendCommand extends ClientToServerPacketType {

    public static final CSendCommand instance = new CSendCommand();

    public static void send(NetworkEntity entity, String[] args) {
        entity.sendPacket(instance.encode(entity, args));
    }

    public static IByteArray get(NetworkEntity entity, String[] args) {
        return instance.encode(entity, args);
    }

    public IByteArray encode(NetworkEntity entity, String[] args) {
        IByteArray array = getWriteArray(entity);
        array.writeUTF16s(args);
        return array;
    }

    @Override
    public void decode(ServerNetworkEntity<IServer> entity, IByteArray data) {
        String[] arguments = data.readUTF16s();
        PlayerExecutor executor = new PlayerExecutor(entity.getServerPlayerData());

        CommandTree tree = new CommandTree(entity.getGameInstance());
        tree.execute(executor, arguments);
    }
}
