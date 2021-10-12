package dev.Hilligans.Ourcraft.ModHandler.Events.Server;

import dev.Hilligans.Ourcraft.ModHandler.Event;
import dev.Hilligans.Ourcraft.Server.IServer;

public class ServerTickEvent extends Event {

    public IServer server;

    public ServerTickEvent(IServer server) {
        this.server = server;
    }


}
