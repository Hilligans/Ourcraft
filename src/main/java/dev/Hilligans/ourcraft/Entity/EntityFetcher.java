package dev.Hilligans.ourcraft.Entity;

import dev.Hilligans.ourcraft.Network.PacketData;

public interface EntityFetcher {

    Entity getEntity(PacketData packetData);

}
