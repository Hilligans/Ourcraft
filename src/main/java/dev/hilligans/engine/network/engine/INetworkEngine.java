package dev.hilligans.engine.network.engine;

import dev.hilligans.engine.GameInstance;
import dev.hilligans.ourcraft.client.Client;
import dev.hilligans.engine.network.Protocol;
import dev.hilligans.ourcraft.server.IServer;
import dev.hilligans.engine.util.IByteArray;
import dev.hilligans.engine.util.registry.IRegistryElement;

import java.util.Iterator;
import java.util.List;

public interface INetworkEngine<T extends NetworkEntity, Q extends NetworkSocket<T>> extends IRegistryElement {

    void addNetworkEntity(T entity);
    void removeNetworkEntity(T entity);

    Iterator<T> getAllNetworkEntities();

    void switchProtocol(T entity, Protocol oldProtocol, Protocol newProtocol);

    List<Q> getNetworkSockets();

    GameInstance getGameInstance();

    Q openClient(Protocol protocol, Client client, String host, String port);
    Q openServer(Protocol protocol, IServer server, String port);

    IByteArray allocByteArray();
    void freeByteArray(IByteArray array);

    @Override
    default String getResourceType() {
        return "network_engine";
    }
}

