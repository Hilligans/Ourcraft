package dev.hilligans.ourcraft.Entity;

import dev.hilligans.ourcraft.Network.PacketData;

public interface EntityFetcher {

    Entity getEntity(PacketData packetData);

}
