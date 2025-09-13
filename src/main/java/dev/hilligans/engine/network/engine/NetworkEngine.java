package dev.hilligans.engine.network.engine;

import dev.hilligans.engine.GameInstance;
import dev.hilligans.engine.network.Protocol;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class NetworkEngine<T extends NetworkEntity, Q extends NetworkSocket<T>> implements INetworkEngine<T, Q> {

    public GameInstance gameInstance;
    public ConcurrentHashMap<T, Boolean> networkEntities;
    public ConcurrentHashMap<Protocol, ConcurrentLinkedQueue<T>> protocolMap = new ConcurrentHashMap<>();


    public NetworkEngine() {}

    @Override
    public void switchProtocol(T entity, Protocol oldProtocol, Protocol newProtocol) {
        ConcurrentLinkedQueue<T> queue = protocolMap.getOrDefault(oldProtocol, null);
        if(queue != null) {
            queue.remove(entity);
        }
        protocolMap.computeIfAbsent(newProtocol, protocol -> new ConcurrentLinkedQueue<>()).add(entity);
    }

    @Override
    public void addNetworkEntity(T entity) {
        networkEntities.put(entity, true);
    }

    @Override
    public void removeNetworkEntity(T entity) {
        networkEntities.remove(entity);
    }

    @Override
    public Iterator<T> getAllNetworkEntities() {
        return networkEntities.keys().asIterator();
    }

    @Override
    public void load(GameInstance gameInstance) {
        this.gameInstance = gameInstance;
    }

    @Override
    public GameInstance getGameInstance() {
        return gameInstance;
    }
}
