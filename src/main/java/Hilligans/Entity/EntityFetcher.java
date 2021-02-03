package Hilligans.Entity;

import Hilligans.Network.PacketData;

public interface EntityFetcher {

    Entity getEntity(PacketData packetData);

}
