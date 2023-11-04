package dev.hilligans.ourcraft.Network;

import dev.hilligans.ourcraft.Data.Other.Server.ServerPlayerData;
import dev.hilligans.ourcraft.Server.IServer;
import dev.hilligans.ourcraft.World.NewWorldSystem.IServerWorld;
import dev.hilligans.ourcraft.World.NewWorldSystem.IWorld;

public interface IServerPacketHandler extends IPacketHandler {

    IServer getServer();

    IServerWorld getWorld();

    ServerPlayerData getServerPlayerData();

}
