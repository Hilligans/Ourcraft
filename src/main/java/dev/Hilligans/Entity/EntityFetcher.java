package dev.Hilligans.Entity;

import dev.Hilligans.Network.PacketData;

public interface EntityFetcher {

    Entity getEntity(PacketData packetData);

}
