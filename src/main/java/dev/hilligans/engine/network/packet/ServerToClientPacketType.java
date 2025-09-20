package dev.hilligans.engine.network.packet;

import dev.hilligans.engine.application.IClientApplication;
import dev.hilligans.engine.network.engine.ClientNetworkEntity;

public abstract class ServerToClientPacketType<T extends IClientApplication> extends PacketType<ClientNetworkEntity<T>> {
}
