package dev.hilligans.ourcraft.entity;

import dev.hilligans.ourcraft.network.IPacketByteArray;

public interface EntityFetcher {

    Entity getEntity(IPacketByteArray packetData);

}
