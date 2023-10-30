package dev.hilligans.ourcraft.Network;

import dev.hilligans.ourcraft.Server.IServer;

public interface IServerPacketHandler extends IPacketHandler {

    IServer getServer();




}
