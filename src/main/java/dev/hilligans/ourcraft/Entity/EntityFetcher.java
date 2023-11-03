package dev.hilligans.ourcraft.Entity;

import dev.hilligans.ourcraft.Network.IPacketByteArray;
import dev.hilligans.ourcraft.Network.PacketData;

public interface EntityFetcher {

    Entity getEntity(IPacketByteArray packetData);

}
