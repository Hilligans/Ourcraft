package dev.hilligans.ourcraft.network.packet;

import dev.hilligans.engine.GameInstance;
import dev.hilligans.engine.data.Tuple;
import dev.hilligans.engine.mod.handler.content.ModContainer;
import dev.hilligans.engine.network.engine.ClientNetworkEntity;
import dev.hilligans.engine.network.engine.NetworkEntity;
import dev.hilligans.engine.network.packet.ServerToClientPacketType;
import dev.hilligans.ourcraft.server.IServer;
import dev.hilligans.engine.util.IByteArray;

import java.util.ArrayList;
import java.util.List;

public class SSendServerInfo extends ServerToClientPacketType {

    public static SSendServerInfo instance = new SSendServerInfo();

    public static void send(NetworkEntity entity, IServer server) {
        entity.sendPacket(instance.encode(entity, server));
    }

    public IByteArray encode(NetworkEntity entity, IServer server) {
        IByteArray array = getWriteArray(entity);

        GameInstance instance = server.getGameInstance();

        array.writeString(server.getVersion());
        array.writeString(server.getMOTD());
        array.writeInt(instance.REGISTRIES.hashCode());

        array.writeList(instance.MOD_LIST.mods, (arr, mod) -> {
            arr.writeString(mod.getModID());
            arr.writeInt(mod.hashCode());
        });

        return array;
    }

    @Override
    public void decode(ClientNetworkEntity entity, IByteArray data) {
        String version = data.readString();
        String motd = data.readString();
        int globalHashcode = data.readInt();

        List<Tuple<String, Integer>> mods = data.readList((array) -> new Tuple<>(array.readString(), array.readInt()));

        System.out.println("Server MOTD:" + motd);
        System.out.println("Server Version:" + version);

        GameInstance gameInstance = entity.getGameInstance();

        boolean differing = gameInstance.REGISTRIES.hashCode() != globalHashcode;

        ArrayList<String> differingMods = new ArrayList<>();
        for(Tuple<String, Integer> mod : mods) {
            ModContainer modContainer = gameInstance.getMod(mod.getTypeA());
            if(modContainer == null) {
                differingMods.add("Missing: " + mod.getTypeA());
            } else if(modContainer.hashCode() != mod.getTypeB()) {
                differingMods.add("Differs: " + mod.getTypeA());
            }
        }

        if(differing) {
            throw new RuntimeException("Cannot join server, content differs, " + differingMods);
        }

        CLogin.send(entity, "hilligans", "ourcraft:unathenticated_scheme");
    }
}
