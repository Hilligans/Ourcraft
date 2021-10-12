package dev.Hilligans.ourcraft.ModHandler.Events.Server;

import dev.Hilligans.ourcraft.ModHandler.Event;
import dev.Hilligans.ourcraft.Server.IServer;

public class ServerTickEvent extends Event {

    public IServer server;

    public ServerTickEvent(IServer server) {
        this.server = server;
    }


}
