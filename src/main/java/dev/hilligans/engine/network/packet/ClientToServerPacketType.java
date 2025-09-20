package dev.hilligans.engine.network.packet;

import dev.hilligans.engine.application.IServerApplication;
import dev.hilligans.engine.network.engine.ServerNetworkEntity;

public abstract class ClientToServerPacketType<T extends IServerApplication> extends PacketType<ServerNetworkEntity<T>> {
}
