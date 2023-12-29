package dev.hilligans.ourcraft.network;

import dev.hilligans.ourcraft.GameInstance;
import dev.hilligans.ourcraft.client.Client;
import dev.hilligans.ourcraft.world.newworldsystem.IWorld;

public interface IClientPacketHandler extends IPacketHandler {

    Client getClient();

    IWorld getWorld();

    GameInstance getGameInstance();
}
