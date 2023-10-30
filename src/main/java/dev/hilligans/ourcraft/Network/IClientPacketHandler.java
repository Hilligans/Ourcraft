package dev.hilligans.ourcraft.Network;

import dev.hilligans.ourcraft.Client.Client;
import dev.hilligans.ourcraft.World.NewWorldSystem.IWorld;

public interface IClientPacketHandler extends IPacketHandler {

    Client getClient();

    IWorld getWorld();

}
