package dev.hilligans.ourcraft.network.engine;

import dev.hilligans.ourcraft.network.Protocol;
import dev.hilligans.ourcraft.util.Side;

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
