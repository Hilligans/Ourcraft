package dev.hilligans.ourcraft.Util;

import dev.hilligans.ourcraft.Network.PacketData;

public class UUID {

    public long[] uuid = new long[2];

    public UUID() {}

    public UUID(PacketData packetData) {
        uuid = packetData.readLongs(2);
    }

    public void put(PacketData packetData) {
        packetData.writeLong(uuid[0]);
        packetData.writeLong(uuid[1]);
    }


}
