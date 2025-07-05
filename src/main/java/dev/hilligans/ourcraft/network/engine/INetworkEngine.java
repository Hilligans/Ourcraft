package dev.hilligans.ourcraft.network.engine;

import dev.hilligans.ourcraft.GameInstance;
import dev.hilligans.ourcraft.network.PacketBase;
import dev.hilligans.ourcraft.network.Protocol;
import dev.hilligans.ourcraft.util.registry.IRegistryElement;

import java.util.Iterator;
import java.util.List;

public interface INetworkEngine<T extends NetworkEntity, Q extends NetworkSocket> extends IRegistryElement {

    void addNetworkEntity(T entity);
    void removeNetworkEntity(T entity);

    Iterator<T> getAllNetworkEntities();

    void sendPacketToAll(Protocol protocol, PacketBase<?> packet);

    void switchProtocol(T entity, Protocol oldProtocol, Protocol newProtocol);

    List<Q> getNetworkSockets();

    GameInstance getGameInstance();

    Q openClient(Protocol protocol, String host, String port);
    Q openServer(Protocol protocol, String port);

    @Override
    default String getResourceType() {
        return "network_engine";
    }
}

