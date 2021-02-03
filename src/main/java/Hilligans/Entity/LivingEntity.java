package Hilligans.Entity;

import Hilligans.Network.PacketData;

public abstract class LivingEntity extends Entity {
    public LivingEntity(float x, float y, float z, int id) {
        super(x, y, z, id);
    }

    public LivingEntity(PacketData packetData) {
        super(packetData);
    }
}
