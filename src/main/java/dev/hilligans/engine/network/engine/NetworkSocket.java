package dev.hilligans.engine.network.engine;

import dev.hilligans.engine.network.Protocol;
import dev.hilligans.engine.util.Side;

import java.util.function.Consumer;

public interface NetworkSocket<T extends NetworkEntity> {

    void connectSocket();
    void closeSocket();

    Side getSide();
    int getPort();
    String getHost();

    Protocol getInitialSendProtocol();
    Protocol getInitialReceiveProtocol();

    void onConnected(Consumer<T> callback);
}
