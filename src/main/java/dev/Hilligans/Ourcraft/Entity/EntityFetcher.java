package dev.Hilligans.Ourcraft.Entity;

import dev.Hilligans.Ourcraft.Network.PacketData;

public interface EntityFetcher {

    Entity getEntity(PacketData packetData);

}
