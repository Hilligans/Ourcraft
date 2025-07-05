package dev.hilligans.ourcraft.network.engine;

import dev.hilligans.ourcraft.network.Protocol;

public interface NetworkEntity {


    Protocol getSendProtocol();
    Protocol getReceiveProtocol();

    INetworkEngine<?, ?> getNetworkEngine();
    void setNetworkEngine(INetworkEngine<?, ?> networkEngine);

    NetworkSocket getNetworkSocket();

    void setAlive(boolean alive);
    boolean isAlive();

}
