package dev.Hilligans.ModHandler.Events.Server;

import dev.Hilligans.ModHandler.Event;
import dev.Hilligans.Server.IServer;

public class ServerTickEvent extends Event {

    public IServer server;

    public ServerTickEvent(IServer server) {
        this.server = server;
    }


}
