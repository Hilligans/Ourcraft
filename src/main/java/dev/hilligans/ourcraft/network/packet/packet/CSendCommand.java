package dev.hilligans.ourcraft.network.packet.packet;

import dev.hilligans.ourcraft.command.CommandTree;
import dev.hilligans.ourcraft.command.executors.PlayerExecutor;
import dev.hilligans.ourcraft.network.engine.NetworkEntity;
import dev.hilligans.ourcraft.network.engine.ServerNetworkEntity;
import dev.hilligans.ourcraft.network.packet.ClientToServerPacketType;
import dev.hilligans.ourcraft.util.IByteArray;

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
    public void decode(ServerNetworkEntity entity, IByteArray data) {
        String[] arguments = data.readUTF16s();
        PlayerExecutor executor = new PlayerExecutor(entity.getServerPlayerData());

        CommandTree tree = new CommandTree(entity.getGameInstance());
        tree.execute(executor, arguments);
    }
}
