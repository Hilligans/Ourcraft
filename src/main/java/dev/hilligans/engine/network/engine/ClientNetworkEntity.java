package dev.hilligans.engine.network.engine;

import dev.hilligans.ourcraft.client.Client;
import dev.hilligans.ourcraft.world.newworldsystem.IWorld;

public interface ClientNetworkEntity extends NetworkEntity {

    Client getClient();

    IWorld getWorld();
}
