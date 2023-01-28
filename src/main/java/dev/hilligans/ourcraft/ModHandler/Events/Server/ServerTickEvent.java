package dev.hilligans.ourcraft.ModHandler.Events.Server;

import dev.hilligans.ourcraft.ModHandler.Event;
import dev.hilligans.ourcraft.Server.IServer;

public class ServerTickEvent extends Event {

    public IServer server;

    public ServerTickEvent(IServer server) {
        this.server = server;
    }


}
