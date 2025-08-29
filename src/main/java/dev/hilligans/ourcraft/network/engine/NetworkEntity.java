package dev.hilligans.ourcraft.network.engine;

import dev.hilligans.ourcraft.GameInstance;
import dev.hilligans.ourcraft.network.PacketBase;
import dev.hilligans.ourcraft.network.Protocol;
import dev.hilligans.ourcraft.util.IByteArray;

public interface NetworkEntity {

    default void switchProtocol(Protocol protocol) {
        switchProtocol(protocol, protocol);
    }
    void switchProtocol(Protocol sendProtocol, Protocol receiveProtocol);

    Protocol getSendProtocol();
    Protocol getReceiveProtocol();

    INetworkEngine<?, ?> getNetworkEngine();
    NetworkEntity setNetworkEngine(INetworkEngine<?, ?> networkEngine);

    NetworkSocket<?> getNetworkSocket();

    void setAlive(boolean alive);
    boolean isAlive();

    void sendPacket(IByteArray data);

    default IByteArray allocByteArray() {
        return getNetworkEngine().allocByteArray();
    }

    default void freeByteArray(IByteArray array) {
        getNetworkEngine().freeByteArray(array);
    }

    GameInstance getGameInstance();
}
