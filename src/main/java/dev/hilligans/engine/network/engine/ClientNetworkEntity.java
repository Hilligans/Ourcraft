package dev.hilligans.engine.network.engine;

import dev.hilligans.engine.application.IClientApplication;

public interface ClientNetworkEntity<T extends IClientApplication> extends NetworkEntity {

    T getClient();

}
