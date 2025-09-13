package dev.hilligans.ourcraft.entity;

import dev.hilligans.engine.network.IPacketByteArray;

public interface EntityFetcher {

    Entity getEntity(IPacketByteArray packetData);

}
