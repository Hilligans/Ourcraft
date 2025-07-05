package dev.hilligans.ourcraft.network.engine;

import dev.hilligans.ourcraft.network.Protocol;
import dev.hilligans.ourcraft.util.Side;

public interface NetworkSocket {

    void connectSocket();
    void closeSocket();

    Side getSide();
    int getPort();
    String getHost();

    Protocol getInitialSendProtocol();
    Protocol getInitialReceiveProtocol();
}
